package com.startup.home.mypage

import androidx.lifecycle.viewModelScope
import com.startup.common.base.BaseViewModel
import com.startup.common.util.ResponseErrorException
import com.startup.common.util.SessionExpireException
import com.startup.domain.model.notice.Notice
import com.startup.domain.usecase.notice.GetNoticeList
import com.startup.home.mypage.model.NoticeViewState
import com.startup.home.mypage.model.NoticeViewStateImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NoticeViewModel @Inject constructor(private val getNoticeList: GetNoticeList) :
    BaseViewModel() {
    private val _state = NoticeViewStateImpl()
    override val state: NoticeViewState get() = _state

    fun fetchNoticeList() {
        getNoticeList.invoke(Unit).onEach { _state.noticeList.update { it } }.catch {
            _state.noticeList.update {
                listOf(
                    Notice(
                        title = "신년을 맞아 새로운 캐릭터가 추가되었어요!",
                        date = "2025년 1월 1일",
                        detail = "안녕하세요.\n" +
                                "워키 팀입니다.\n" +
                                "\n" +
                                "2025년 12월 31일부터 스팟 방문 정책이 변경됩니다. 한 번 방문한 스팟은 3일간의 제한 기간이 적용된 후 다시 방문할 수 있습니다. \n" +
                                "서비스 이용에 참고해 주시기 바랍니다.\n" +
                                "\n" +
                                "■ 적용 일정\n" +
                                "2025년 1월 1일 (수요일)\n" +
                                "\n" +
                                "■ 변경 내용\n" +
                                "기존: 한 번 방문한 스팟도 매일 방문 가능\n" +
                                "변경: 한 번 방문한 스팟은 3일간의 제한 기간 후 방문 가능.\n" +
                                "\n" +
                                "워키 팀은 앞으로도 더 나은 서비스를 제공해드릴 수 있도록 최선을 다하겠습니다.\n" +
                                "감사합니다.\n" +
                                "\n" +
                                "워키 팀 드림" + "안녕하세요.\n" +
                                "워키 팀입니다.\n" +
                                "\n" +
                                "2025년 12월 31일부터 스팟 방문 정책이 변경됩니다. 한 번 방문한 스팟은 3일간의 제한 기간이 적용된 후 다시 방문할 수 있습니다. \n" +
                                "서비스 이용에 참고해 주시기 바랍니다.\n" +
                                "\n" +
                                "■ 적용 일정\n" +
                                "2025년 1월 1일 (수요일)\n" +
                                "\n" +
                                "■ 변경 내용\n" +
                                "기존: 한 번 방문한 스팟도 매일 방문 가능\n" +
                                "변경: 한 번 방문한 스팟은 3일간의 제한 기간 후 방문 가능.\n" +
                                "\n" +
                                "워키 팀은 앞으로도 더 나은 서비스를 제공해드릴 수 있도록 최선을 다하겠습니다.\n" +
                                "감사합니다.\n" +
                                "\n" +
                                "워키 팀 드림"
                    ), Notice(
                        title = "신년을 맞아 새로운 캐릭터가 추가되었어요!",
                        date = "2025년 1월 1일",
                        detail = "~~~"
                    ), Notice(
                        title = "신년을 맞아 새로운 캐릭터가 추가되었어요!",
                        date = "2025년 1월 1일",
                        detail = "~~~"
                    ), Notice(
                        title = "신년을 맞아 새로운 캐릭터가 추가되었어요!",
                        date = "2025년 1월 1일",
                        detail = "~~~"
                    ), Notice(
                        title = "신년을 맞아 새로운 캐릭터가 추가되었어요!",
                        date = "2025년 1월 1일",
                        detail = "~~~"
                    ), Notice(
                        title = "신년을 맞아 새로운 캐릭터가 추가되었어요!",
                        date = "2025년 1월 1일",
                        detail = "~~~"
                    ), Notice(
                        title = "신년을 맞아 새로운 캐릭터가 추가되었어요!",
                        date = "2025년 1월 1일",
                        detail = "~~~"
                    ), Notice(
                        title = "신년을 맞아 새로운 캐릭터가 추가되었어요!",
                        date = "2025년 1월 1일",
                        detail = "~~~"
                    ), Notice(
                        title = "신년을 맞아 새로운 캐릭터가 추가되었어요!",
                        date = "2025년 1월 1일",
                        detail = "~~~"
                    ), Notice(
                        title = "신년을 맞아 새로운 캐릭터가 추가되었어요!",
                        date = "2025년 1월 1일",
                        detail = "~~~"
                    ), Notice(
                        title = "신년을 맞아 새로운 캐릭터가 추가되었어요!",
                        date = "2025년 1월 1일",
                        detail = "~~~"
                    ), Notice(
                        title = "신년을 맞아 새로운 캐릭터가 추가되었어요!",
                        date = "2025년 1월 1일",
                        detail = "~~~"
                    ), Notice(
                        title = "신년을 맞아 새로운 캐릭터가 추가되었어요!",
                        date = "2025년 1월 1일",
                        detail = "~~~"
                    ), Notice(
                        title = "신년을 맞아 새로운 캐릭터가 추가되었어요!",
                        date = "2025년 1월 1일",
                        detail = "~~~"
                    ), Notice(
                        title = "신년을 맞아 새로운 캐릭터가 추가되었어요!",
                        date = "2025년 1월 1일",
                        detail = "~~~"
                    ), Notice(
                        title = "신년을 맞아 새로운 캐릭터가 추가되었어요!",
                        date = "2025년 1월 1일",
                        detail = "~~~"
                    ), Notice(
                        title = "신년을 맞아 새로운 캐릭터가 추가되었어요!",
                        date = "2025년 1월 1일",
                        detail = "~~~"
                    )
                )
            }
            when (it) {
                is SessionExpireException -> {
                    // TODO 처리
                }

                is ResponseErrorException -> {
                    // TODO 처리
                }
            }
        }.launchIn(viewModelScope)
    }
}