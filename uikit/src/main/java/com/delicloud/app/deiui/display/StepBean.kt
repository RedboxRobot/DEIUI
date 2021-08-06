package com.delicloud.app.deiui.display

/**
 * 日期：16/9/3 00:36
 * 步骤条实体
 * 描述：
 */
class StepBean {

    var name: String? = null
    /**
     * 当前步骤状态，未完成，正在进行，已完成
     */
    var state: Int = 0
    var index: Int = 0

    constructor(name: String, state: Int, index: Int) {
        this.name = name
        this.state = state
        this.index = index
    }

    companion object {
        const val STEP_UNDO = -1//未完成  undo step
        const val STEP_CURRENT = 0//正在进行 current step
        const val STEP_COMPLETED: Int = 1//已完成 completed step
    }
}
