import { useState } from "react";
import { getAccessToken } from "../assets/tokenActions";
import axios from "axios";

interface CommentListItemType {
	id: number;
	text: string;
	voteCount: number;
	voteStatus: boolean;
	memberId: number;
	memberName: string;
	createdAt: string;
	isRemoved?: boolean;
	children?: any[];
	parentId?: number;
}

const CommentListItem = ({
	data,
	isChild,
	postId,
	memberId,
	fetchData,
}: {
	data: CommentListItemType;
	isChild: boolean;
	postId: string | number;
	memberId: string | number;
	fetchData: any;
}) => {
	const api = process.env.REACT_APP_API_URL;
	const [isUpdating, setIsUpdating] = useState<boolean>(false);
	const [comment, setComment] = useState<string>(data.text);
	const [isReplyWriting, setIsReplyWriting] = useState<boolean>(false);
	const [reply, setReply] = useState<string>("");

	const handleClickCommentUpdate = () => {
		setIsUpdating(true);
	};
	const handleChangeComment = (event: any) => {
		setComment(event.target.value);
	};
	const handleClickCommentUpdateComplete = () => {
		//댓글 Update
		const accessToken = getAccessToken();
		if (accessToken) {
			axios
				.patch(
					`${api}/api/v1/comment/update/${data.id}`,
					{ text: comment },
					{
						headers: { Authorization: accessToken },
					}
				)
				.then((res) => {
					alert("댓글이 수정되었습니다.");
					fetchData();
				})
				.catch((_) => {
					alert("댓글 수정에 실패했습니다.");
				});
		}
		setIsUpdating(false);
	};
	const handleClickCommentDelete = () => {
		//댓글 delete
		//팝업창으로 대체할 것
		let isDeleteYes = window.confirm("댓글을 삭제하시겠습니까?");
		if (isDeleteYes) {
			const accessToken = getAccessToken();
			if (accessToken) {
				axios
					.get(`${api}/api/v1/comment/${data.id}`, {
						headers: { Authorization: accessToken },
					})
					.then((res) => {
						alert("댓글이 삭제되었습니다.");
						fetchData();
					})
					.catch((_) => {
						alert("댓글 삭제 오류가 발생했습니다.");
					});
			}
		}
	};
	const handleClicktoggleReply = () => {
		setIsReplyWriting((prev) => !prev);
	};
	const handleChangeReply = (event: any) => {
		setReply(event.target.value);
	};
	const handleClickReplySubmit = () => {
		const accessToken = getAccessToken();
		//답글 Create
		if (accessToken) {
			axios
				.post(
					`${api}/api/v1/comment/create`,
					{
						text: reply,
						postId: postId,
						parentCommentId: data.id,
					},
					{
						headers: { Authorization: accessToken },
					}
				)
				.then((res) => {
					alert("답글이 등록되었습니다.");
					setReply("");
					setIsReplyWriting(false);
					fetchData();
				})
				.catch((_) => {
					alert("답글 등록에 실패했습니다.");
				});
		}
	};

	return (
		<>
			{data.isRemoved ? (
				<div>삭제된 댓글입니다.</div>
			) : (
				<>
					<div className="comment_head">
						<div className="comment_user_profile">
							<div className="user_img_wrap"></div>
							<h5 className="user_name">{data.memberName}</h5>
							<div className="comment_ceated_time">{data.createdAt}</div>
						</div>
						<div className="comment_likes">{data.voteCount}</div>
					</div>
					{isUpdating ? (
						<div className="comment_body">
							<textarea
								className="comment_content_textarea"
								onChange={(e) => handleChangeComment(e)}
							>
								{comment}
							</textarea>
							<div className="comment_update_btn_wrap">
								<button
									className="comment_btn update"
									onClick={handleClickCommentUpdateComplete}
								>
									수정완료
								</button>
								<button
									className="comment_btn delete"
									onClick={handleClickCommentDelete}
								>
									삭제
								</button>
							</div>
						</div>
					) : (
						<div className="comment_body">
							<div className="comment_content">{comment}</div>
							{memberId === data.memberId ? (
								<div className="comment_update_btn_wrap">
									<button
										className="comment_btn update"
										onClick={handleClickCommentUpdate}
									>
										수정
									</button>
									<button
										className="comment_btn delete"
										onClick={handleClickCommentDelete}
									>
										삭제
									</button>
								</div>
							) : null}
						</div>
					)}
					{isChild ? null : (
						<div className="comment_reply_btns_wrap">
							{/* {data.children!.length > 0 ? (
								<button className="comment_reply_btn toggle_view_child_comments show">
									답글 더보기
								</button>
							) : null} */}
							{isReplyWriting ? (
								<div className="reply_create_wrap">
									<div>
										<textarea
											placeholder="답글을 작성하세요"
											onChange={(e) => handleChangeReply(e)}
											value={reply}
										></textarea>
									</div>
									<div className="reply_create_btns">
										<button
											className="reply_create_btn reply_cancel"
											onClick={handleClicktoggleReply}
										>
											취소
										</button>
										<button
											className="reply_create_btn reply_submit"
											onClick={handleClickReplySubmit}
										>
											작성 완료
										</button>
									</div>
								</div>
							) : (
								<button
									className="comment_reply_btn create_comment_child"
									onClick={handleClicktoggleReply}
								>
									답글
								</button>
							)}
						</div>
					)}
				</>
			)}
		</>
	);
};

export default CommentListItem;
