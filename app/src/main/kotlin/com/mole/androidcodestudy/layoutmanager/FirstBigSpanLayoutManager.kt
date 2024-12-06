package com.mole.androidcodestudy.layoutmanager

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/12/04
 *     desc   : 竖向的，如果数量超过9个，第一个占满 2 行 2 列
 *     version: 1.0
 * </pre>
 */
class FirstBigSpanLayoutManager : RecyclerView.LayoutManager() {
    private var childFrames = mutableMapOf<Int, Rect>()
    private var layoutStart = 0
    private var layoutEnd = 0
    private val size
        get() = height

    /**
     * Current scroll amount
     */
    protected var scroll = 0

    /**
     * 滑动方向
     * START向上
     * END 向下
     */
    enum class Direction {
        START, END
    }

    private val mSpanSizeLookup = CustomSpanSizeLookup()
    private lateinit var mRectHelper: RectHelper

    /**
     * 记录 [scrollToPosition] 的位置
     */
    private var pendingScrollToPosition: Int? = null

    /**
     * First visible position in layout - changes with recycling
     */
    open val firstVisiblePosition: Int
        get() {
            if (childCount == 0) {
                return 0
            }
            return getPosition(getChildAt(0)!!)
        }

    /**
     * 暂时只考虑竖向垂直的布局
     */
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    override fun scrollToPosition(position: Int) {
        pendingScrollToPosition = position
        super.scrollToPosition(position)
    }

    //override fun isAutoMeasureEnabled(): Boolean {
    //    return true
    //}

    /**
     * 非自测量模式，需要根据位置决定 item 的宽高
     */
    override fun onMeasure(
        recycler: RecyclerView.Recycler, state: RecyclerView.State, widthSpec: Int, heightSpec: Int
    ) {
        //super.onMeasure(recycler, state, widthSpec, heightSpec)
        //val widthSize = MeasureSpec.getSize(widthSpec)
        //return
        if (state.itemCount == 0) {
            super.onMeasure(recycler, state, widthSpec, heightSpec)
            return
        }

        val widthSize = View.MeasureSpec.getSize(widthSpec) - paddingLeft - paddingRight
        val itemView = recycler.getViewForPosition(0)

        val horizonDecoration = getLeftDecorationWidth(itemView) + getRightDecorationWidth(itemView)
        val verticalDecoration =
            getTopDecorationHeight(itemView) + getBottomDecorationHeight(itemView)
        val itemWidth = (widthSize - horizonDecoration * 3) / 3
        //TODO 此时回收 view 就会崩溃？  记住回收 view
        //并且因为设置了完全的高度，导致加载了全部的 item，没法利用 rc 的回收复用机制
        //detachAndScrapView(itemView, recycler)
        //如果有 9 个
        if (state.itemCount >= CHANGE_COUNT) {
            //    获取RecyclerView 的宽度
            //    获取 itemDecoration 的水平margin
            //    计算 横竖 1：:1 ，3个 item 的情况下，每个 item实际宽度，高度
            //  获取 itemDecoration 的竖直margin
            //    第一个的高度是 2倍数，所以前 3 个 item 等于原先的两行
            //    剩下的 item 正常排列
            //    获取一共占据多少行，算出实际高度

            val rowSize = 2 + (state.itemCount - 1) / HORIZON_COUNT
            val newHeight = rowSize * (itemWidth + verticalDecoration) + paddingTop + paddingBottom
            setMeasuredDimension(widthSize, newHeight)
        } else {
            val rowSize = 1 + (state.itemCount) / HORIZON_COUNT
            val newHeight = rowSize * (itemWidth + verticalDecoration) + paddingTop + paddingBottom
            setMeasuredDimension(widthSize, newHeight)
        }
    }

    override fun scrollVerticallyBy(
        dy: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State
    ): Int {
        return scrollBy(dy, recycler, state)
    }

    protected open fun scrollBy(
        delta: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State
    ): Int {
        // If there are no view or no movement, return
        if (delta == 0) {
            return 0
        }

        val canScrollBackwards = (firstVisiblePosition) >= 0 && 0 < scroll && delta < 0

        val canScrollForward =
            (firstVisiblePosition + childCount) <= state.itemCount && (scroll + size) < (layoutEnd + paddingBottom) &&
                    delta > 0

        // If can't scroll forward or backwards, return
        if (!(canScrollBackwards || canScrollForward)) {
            return 0
        }

        val correctedDistance = scrollBy(-delta, state)

        val direction = if (delta > 0) Direction.END else Direction.START

        recycleChildrenOutOfBounds(direction, recycler)

        fillGap(direction, recycler, state)

        return -correctedDistance
    }

    override fun canScrollVertically(): Boolean {
        //如果 item 不多，不需要考虑滚动
        if (childFrames.isNotEmpty()) {
            return (childFrames[childFrames.size - 1]?.bottom ?: 0) > height
        }
        return false
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        //每次重新布局，都要使用新的RectHelper，方便计算布局区域
        mRectHelper = RectHelper(this)
        layoutStart = paddingTop
        //滑动时，下面加载出的一行的高度？
        layoutEnd = if (scroll != 0) {
            val currentRow = (scroll - layoutStart) / mRectHelper.itemSize
            currentRow * mRectHelper.itemSize
        } else {
            paddingBottom
        }
        //如果之前已经布局过，需要清除缓存，并轻量级的回收View
        childFrames.clear()
        detachAndScrapAttachedViews(recycler)

        //记录一下时间
        val start = System.currentTimeMillis()
        //开始填充 View，注意是 state.itemCount
        for (i in 0 until state.itemCount) {
            val spanSize = mSpanSizeLookup.getSpanSize(state, i)
            val childRect = mRectHelper.findRect(i, spanSize)
            mRectHelper.pushRect(i, childRect)
        }

        val elapsed = System.currentTimeMillis() - start
        Log.d("pushRectTime", "Elapsed time: $elapsed ms")

        // Restore scroll position based on first visible view
        val pendingScrollToPosition = pendingScrollToPosition
        if (itemCount != 0 && pendingScrollToPosition != null && pendingScrollToPosition >= HORIZON_COUNT) {

            val currentRow =
                mRectHelper.rows.filter { (_, value) -> value.contains(pendingScrollToPosition) }.keys.firstOrNull()

            if (currentRow != null) {
                scroll = paddingTop + (currentRow * mRectHelper.itemSize)
            }

            this.pendingScrollToPosition = null
        }

        //    开始填充 item
        fillGap(Direction.END, recycler, state)

        recycleChildrenOutOfBounds(Direction.END, recycler)

        //布局修改后，发现高度过剩了，需要修正
        val overScroll = scroll + size - layoutEnd - paddingBottom
        val isLastItemInScreen =
            (0 until childCount).map { getPosition(getChildAt(it)!!) }.contains(itemCount - 1)
        val allItemsInScreen = itemCount == 0 || (firstVisiblePosition == 0 && isLastItemInScreen)
        if (!allItemsInScreen && overScroll > 0) {
            // If we are, fix it
            scrollBy(overScroll, state)
            fillBefore(recycler, state)
        }
    }

    protected open fun scrollBy(distance: Int, state: RecyclerView.State): Int {
        val paddingEndLayout = paddingBottom

        val start = 0
        val end = layoutEnd + mRectHelper.itemSize + paddingEndLayout

        scroll -= distance

        var correctedDistance = distance

        // Correct scroll if was out of bounds at start
        if (scroll < start) {
            correctedDistance += scroll
            scroll = start
        }

        // Correct scroll if it would make the layout scroll out of bounds at the end
        if (scroll + size > end && (firstVisiblePosition + childCount + HORIZON_COUNT) >= state.itemCount) {
            correctedDistance -= (end - scroll - size)
            scroll = end - size
        }

        offsetChildrenVertical(correctedDistance)

        return correctedDistance
    }

    protected open fun recycleChildrenOutOfBounds(
        direction: Direction, recycler: RecyclerView.Recycler
    ) {
        if (direction == Direction.END) {
            recycleChildrenFromStart(direction, recycler)
        } else {
            recycleChildrenFromEnd(direction, recycler)
        }
    }

    /**
     * Recycle views from start to first visible item
     */
    protected open fun recycleChildrenFromStart(
        direction: Direction, recycler: RecyclerView.Recycler
    ) {
        val childCount = childCount
        val start = paddingTop

        val toDetach = mutableListOf<View>()

        for (i in 0 until childCount) {
            val child = getChildAt(i)!!
            val childEnd = getChildEnd(child)

            if (childEnd < start) {
                toDetach.add(child)
            }
        }

        for (child in toDetach) {
            removeAndRecycleView(child, recycler)
            updateEdgesWithRemovedChild(child, direction)
        }
    }

    /**
     * Recycle views from end to last visible item
     */
    protected open fun recycleChildrenFromEnd(
        direction: Direction, recycler: RecyclerView.Recycler
    ) {
        val childCount = childCount
        val end = size + paddingBottom

        val toDetach = mutableListOf<View>()

        for (i in (0 until childCount).reversed()) {
            val child = getChildAt(i)!!
            val childStart = getChildStart(child)

            if (childStart > end) {
                toDetach.add(child)
            }
        }

        for (child in toDetach) {
            removeAndRecycleView(child, recycler)
            updateEdgesWithRemovedChild(child, direction)
        }
    }

    /**
     * Update layout edges when views are recycled
     */
    protected open fun updateEdgesWithRemovedChild(view: View, direction: Direction) {
        val childStart = getChildStart(view) + scroll
        val childEnd = getChildEnd(view) + scroll

        if (direction == Direction.END) { // Removed from start
            layoutStart = paddingTop + childEnd
        } else if (direction == Direction.START) { // Removed from end
            layoutEnd = paddingBottom + childStart
        }


    }


    /**
     *
     */
    protected open fun fillGap(
        direction: Direction, recycler: RecyclerView.Recycler, state: RecyclerView.State
    ) {
        if (direction == Direction.END) {
            fillAfter(recycler, state)
        } else {
            fillBefore(recycler, state)
        }
    }

    /**
     * Fill gaps before the current visible scroll position
     * @param recycler Recycler
     */
    protected open fun fillBefore(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        val currentRow = (scroll - paddingTop) / mRectHelper.itemSize
        val lastRow = (scroll + size - paddingTop) / mRectHelper.itemSize

        for (row in (currentRow until lastRow).reversed()) {
            val positionsForRow = mRectHelper.findPositionsForRow(row).reversed()

            for (position in positionsForRow) {
                if (findViewByPosition(position) != null) continue
                makeAndAddView(position, Direction.START, recycler, state)
            }
        }
    }

    /**
     * Fill gaps after the current layouted views
     * @param recycler Recycler
     */
    protected open fun fillAfter(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        val visibleEnd = scroll + size

        val lastAddedRow = layoutEnd / mRectHelper.itemSize
        val lastVisibleRow = visibleEnd / mRectHelper.itemSize

        for (rowIndex in lastAddedRow..lastVisibleRow) {
            val row = mRectHelper.rows[rowIndex] ?: continue

            for (itemIndex in row) {

                if (findViewByPosition(itemIndex) != null) continue

                makeAndAddView(itemIndex, Direction.END, recycler, state)
            }
        }
    }

    protected open fun makeAndAddView(
        position: Int,
        direction: Direction,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ): View {
        val view = makeView(position, direction, recycler, state)

        if (direction == Direction.END) {
            addView(view)
        } else {
            addView(view, 0)
        }

        return view
    }

    protected open fun makeView(
        position: Int,
        direction: Direction,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ): View {
        val view = recycler.getViewForPosition(position)
        measureChild(position, view, state)
        layoutChild(position, view)

        return view
    }

    protected open fun measureChild(position: Int, view: View, state: RecyclerView.State) {

        val freeRectsHelper = this.mRectHelper

        val itemWidth = freeRectsHelper.itemSize
        val itemHeight = freeRectsHelper.itemSize

        val spanSize = mSpanSizeLookup.getSpanSize(state, position)

        val usedSpan = spanSize.width

        if (usedSpan > HORIZON_COUNT || usedSpan < 1) {
            throw Exception("超出 SpanSize 范围")
        }

        // This rect contains just the row and column number - i.e.: [0, 0, 1, 1]
        val rect = freeRectsHelper.findRect(position, spanSize)

        // Multiply the rect for item width and height to get positions
        val left = rect.left * itemWidth
        val right = rect.right * itemWidth
        val top = rect.top * itemHeight
        val bottom = rect.bottom * itemHeight

        val insetsRect = Rect()
        calculateItemDecorationsForChild(view, insetsRect)

        // Measure child
        val width = right - left - insetsRect.left - insetsRect.right
        val height = bottom - top - insetsRect.top - insetsRect.bottom
        val layoutParams = view.layoutParams
        layoutParams.width = width
        layoutParams.height = height
        measureChildWithMargins(view, width, height)

        // Cache rect
        childFrames[position] = Rect(left, top, right, bottom)
    }

    /**
     * Layout child once it's measured and its position cached
     */
    protected open fun layoutChild(position: Int, view: View) {
        val frame = childFrames[position]

        if (frame != null) {
            val scroll = this.scroll

            val startPadding = paddingTop

            layoutDecorated(
                view,
                frame.left + paddingLeft,
                frame.top - scroll + startPadding,
                frame.right + paddingLeft,
                frame.bottom - scroll + startPadding
            )
        }

        // A new child was layouted, layout edges change
        updateEdgesWithNewChild(view)
    }

    protected open fun updateEdgesWithNewChild(view: View) {
        val childStart = getChildStart(view) + scroll + paddingTop

        if (childStart < layoutStart) {
            layoutStart = childStart
        }

        val newLayoutEnd = childStart + mRectHelper.itemSize

        if (newLayoutEnd > layoutEnd) {
            layoutEnd = newLayoutEnd
        }
    }

    protected open fun getChildStart(child: View): Int {
        return getDecoratedTop(child)
    }

    protected open fun getChildEnd(child: View): Int {
        return getDecoratedBottom(child)
    }

    /**
     * 查找对应位置的 Item 的大小
     */
    class CustomSpanSizeLookup {
        fun getSpanSize(state: RecyclerView.State, position: Int): SpanSize {
            return when (position) {
                0 -> {
                    if (state.itemCount >= CHANGE_COUNT) {
                        SpanSize(2, 2)
                    } else {
                        SpanSize(1, 1)
                    }
                }

                else -> {
                    SpanSize(1, 1)
                }
            }
        }
    }

    //<editor-fold desc="Decoration相关">
    override fun getDecoratedMeasuredWidth(child: View): Int {
        val position = getPosition(child)
        return childFrames[position]!!.width()
    }

    override fun getDecoratedMeasuredHeight(child: View): Int {
        val position = getPosition(child)
        return childFrames[position]!!.height()
    }

    override fun getDecoratedTop(child: View): Int {
        val position = getPosition(child)
        val decoration = getTopDecorationHeight(child)
        var top = childFrames[position]!!.top + decoration

        top -= scroll

        return top
    }

    override fun getDecoratedRight(child: View): Int {
        val position = getPosition(child)
        val decoration = getLeftDecorationWidth(child) + getRightDecorationWidth(child)
        var right = childFrames[position]!!.right + decoration

        return right
    }

    override fun getDecoratedLeft(child: View): Int {
        val position = getPosition(child)
        val decoration = getLeftDecorationWidth(child)
        var left = childFrames[position]!!.left + decoration

        return left
    }

    override fun getDecoratedBottom(child: View): Int {
        val position = getPosition(child)
        val decoration = getTopDecorationHeight(child) + getBottomDecorationHeight(child)
        var bottom = childFrames[position]!!.bottom + decoration

        bottom -= scroll - paddingTop
        return bottom
    }
    //</editor-fold>

    /**
     * 保存对应位置的 item 大小
     */
    class RectHelper(val layoutManager: FirstBigSpanLayoutManager) {

        /**
         * Comparator to sort free rects by position, based on orientation
         */
        private val rectComparator = Comparator<Rect> { rect1, rect2 ->
            if (rect1.top == rect2.top) {
                if (rect1.left < rect2.left) {
                    -1
                } else {
                    1
                }
            } else {
                if (rect1.top < rect2.top) {
                    -1
                } else {
                    1
                }
            }
        }
        val size: Int
            get() = layoutManager.width - layoutManager.paddingLeft - layoutManager.paddingRight

        //行数对应 item position 的集合
        val rows = mutableMapOf<Int, Set<Int>>()
        val itemSize: Int
            get() = size / HORIZON_COUNT

        /**
         * 空闲区域，每次塞入新 item 后，都会计算剩余的空闲区域，因为可能被分成好多分，所以用 list
         */
        private val freeRect = mutableListOf<Rect>()

        //位置对应区域的缓存
        private val rectCache = mutableMapOf<Int, Rect>()

        fun findRect(position: Int, spanSize: SpanSize): Rect {
            return rectCache[position] ?: findRectForSpanSize(spanSize)
        }

        fun findPositionsForRow(rowPosition: Int): Set<Int> {
            return rows[rowPosition] ?: emptySet()
        }

        /**
         * 在空闲区域中寻找可以放对应 spanSize 的区域
         */
        private fun findRectForSpanSize(spanSize: SpanSize): Rect {
            val lane = freeRect.first {
                val itemRect =
                    Rect(it.left, it.top, it.left + spanSize.width, it.top + spanSize.height)
                it.contains(itemRect)
            }
            return Rect(lane.left, lane.top, lane.left + spanSize.width, lane.top + spanSize.height)
        }

        /**
         * 将区域数据塞入 rectCache
         * 把位置数据塞入 rows
         * 从 freeRect 中移除 rect
         */
        fun pushRect(position: Int, rect: Rect) {
            val start = rect.top
            //从顶部行开始塞入 position
            //如果之前没有这一行，就新建一行
            val startRow = rows[start]?.toMutableSet() ?: mutableSetOf()
            //把第 n 个 item 放到了其对应位置的行上
            startRow.add(position)
            rows[start] = startRow

            //  底部行塞入 position
            val end = rect.bottom
            val endROw = rows[end]?.toMutableSet() ?: mutableSetOf()
            endROw.add(position)
            rows[end] = endROw

            //为什么只需要顶部和底部行塞入 position？
            //因为rows 要考虑从顶部和底部恢复 item 的逻辑 fillAfter和 fillBefore。
            //只需要达到边界就完成判断了，所以不需要设置中间的行

            rectCache[position] = rect

            subtract(rect)
        }

        /**
         * 找出剩余的空间
         */
        private fun subtract(subtractedRect: Rect) {
            //找到包含的区域 == 找到放置 item 的空闲区域
            //找到相邻的区域 == 避免新增多余的空闲区域
            val interestingRect = freeRect.filter {
                it.isAdjacentTo(subtractedRect) || it.intersects(subtractedRect)
            }
            //可能的新的空闲区域
            val possibleNewRect = mutableListOf<Rect>()
            //相邻的区域
            val adjacentRect = mutableListOf<Rect>()

            for (free in interestingRect) {
                if (free.isAdjacentTo(subtractedRect) && !subtractedRect.contains(free)) {
                    //相邻，并且不被包含 == 相邻的空闲区域,并且不是被包含在区域内，和该 item 无关
                    adjacentRect.add(free)
                } else {
                    //只剩
                    //1.可以放 item 的区域
                    //2.放 item 被覆盖的空闲区域
                    freeRect.remove(free)

                    //空闲区域 在 item 的左边有剩余，可能剩下的空闲区域（左半边）
                    if (free.left < subtractedRect.left) {
                        possibleNewRect.add(
                            Rect(
                                free.left, free.top, subtractedRect.left, free.bottom
                            )
                        )
                    }

                    //如果空闲区域在 item 右边有剩余，就留下空闲区域右边位置
                    if (free.right > subtractedRect.right) {
                        possibleNewRect.add(
                            Rect(
                                subtractedRect.right, free.top, free.right, free.bottom
                            )
                        )
                    }

                    //如果空闲区域在 item 上边有剩余，就留下空闲区域上半部分
                    if (free.top < subtractedRect.top) {
                        possibleNewRect.add(
                            Rect(
                                free.left, free.top, free.right, subtractedRect.top
                            )
                        )
                    }

                    //如果空闲区域在 item 下边有剩余，就留下空闲区域下半部分
                    if (free.bottom > subtractedRect.bottom) {
                        possibleNewRect.add(
                            Rect(
                                free.left, subtractedRect.bottom, free.right, free.bottom
                            )
                        )
                    }
                }
            }

            // 上面的寻找方法一定存在多余的空闲区域
            for (rect in possibleNewRect) {
                //如果被相邻区域包含，不用新增
                val isAdjacent = adjacentRect.any {
                    it != rect && it.contains(rect)
                }
                if (isAdjacent) continue

                //被其他可能的空闲区域包含，不用新增 ？？？什么情况下会出现
                val isContained = possibleNewRect.any {
                    it != rect && it.contains(rect)
                }
                if (isContained) continue
                freeRect.add(rect)
            }

            //空闲区域按照从左到右，从上到下的顺序排序
            freeRect.sortWith(rectComparator)
        }

        init {
            //初始化空闲区域
            val initialFreeRect = Rect(
                0, 0, HORIZON_COUNT, Int.MAX_VALUE
            )
            freeRect.add(initialFreeRect)
        }

        fun Rect.isAdjacentTo(rect: Rect): Boolean {
            return (this.right == rect.left || this.top == rect.bottom || this.left == rect.right || this.bottom == rect.top)
        }

        fun Rect.intersects(rect: Rect): Boolean {
            return this.intersects(rect.left, rect.top, rect.right, rect.bottom)
        }
    }

    /**
     * 记录 SpanSize
     */
    class SpanSize(val width: Int, val height: Int)

    companion object {
        private const val CHANGE_COUNT = 9
        private const val HORIZON_COUNT = 3
    }
}
