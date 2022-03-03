package com.gunyoung.infokt.view.util

import org.springframework.web.servlet.ModelAndView

class ModelAndPageView : ModelAndView() {

    companion object {
        const val INDEX_NUM_PER_PAGE = 5

        const val CURRENT_PAGE = "currentPage"
        const val START_INDEX = "startIndex"
        const val LAST_INDEX = "lastIndex"
    }

    fun setPageNumbers(currentPage: Int, totalPageNum: Long) {
        addObject(CURRENT_PAGE, currentPage)
        addObject(START_INDEX, (currentPage/INDEX_NUM_PER_PAGE)* INDEX_NUM_PER_PAGE + 1)
        addObject(
            LAST_INDEX,
            if (currentPage / INDEX_NUM_PER_PAGE * INDEX_NUM_PER_PAGE + INDEX_NUM_PER_PAGE > totalPageNum) {
                totalPageNum
            } else {
                currentPage / INDEX_NUM_PER_PAGE * INDEX_NUM_PER_PAGE + INDEX_NUM_PER_PAGE
            }
        )
    }
}