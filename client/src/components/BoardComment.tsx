import { useState, useEffect } from "react";
import Button from "../components/Button";
import CommentListItem from "./CommentListItem";
import axios from "axios";
import { getAccessToken } from "../assets/tokenActions";
import dummyCommentsData from "./../data/boardCommentsData.json";

const BoardComment = ({
	postId,
	memberId,
}: {
	postId: string | number;
	memberId: string | number;
}) => {
	const api = process.env.REACT_APP_API_URL;
	const [comment, setComment] = useState<string>("");
	const [commentList, setCommentList] = useState<any>();

	const fetchCommentData = () => {
		const accessToken = getAccessToken();
		if (accessToken) {
			axios
				.get(`${api}/api/v1/comment?post=${postId}`, {
					headers: { Authorization: accessToken },
				})
				.then((res) => {
					// console.log(res.data.data); 성공적으로 삭제해도 isRemoved 떠서 확인필요
					setCommentList(res.data.data);
				})
				.catch((_) => {});
		}
	};

	useEffect(() => {
		//댓글목록 Read
		fetchCommentData();
	}, []);

	const handleChangeComment = (event: any) => {
		setComment(event.target.value);
	};
	const handleClickCommentSubmit = () => {
		const accessToken = getAccessToken();
		//댓글 Create
		if (accessToken) {
			axios
				.post(
					`${api}/api/v1/comment/create`,
					{
						text: comment,
						postId: postId,
					},
					{
						headers: { Authorization: accessToken },
					}
				)
				.then((res) => {
					alert("댓글이 등록되었습니다.");
					setComment("");
					fetchCommentData();
				})
				.catch((_) => {
					alert("댓글 등록에 실패했습니다.");
				});
		}
	};

	return (
		<div className="board_comment">
			<div className="comment_write">
				<dl>
					<dt className="comment_write_title">댓글 작성</dt>
					<dd>
						<textarea
							placeholder="댓글을 작성하세요"
							onChange={(e) => handleChangeComment(e)}
							value={comment}
						></textarea>
					</dd>
				</dl>
				<Button
					buttonType="primary"
					buttonSize="big"
					buttonLabel="댓글 입력"
					onClick={handleClickCommentSubmit}
				/>
			</div>
			{commentList ? (
				<div className="comments_list_wrap">
					<h4 className="comments_title">댓글 목록</h4>
					<ul className="comment_list">
						{/* 기본  */}
						{commentList?.map((el: any, idx: number) => {
							return (
								<li className="comment_list_item" key={idx}>
									<CommentListItem
										data={el}
										isChild={false}
										postId={postId}
										memberId={memberId}
										fetchData={fetchCommentData}
									/>
									{el.children.length > 0 ? (
										<div className="comment_children_wrap">
											<ul className="comment_children_list">
												{el.children.map((child: any, childIdx: number) => (
													<li className="comment_list_item">
														<CommentListItem
															data={child}
															isChild={true}
															postId={postId}
															memberId={memberId}
															fetchData={fetchCommentData}
														/>
													</li>
												))}
											</ul>
										</div>
									) : null}
								</li>
							);
						})}

						{/* 답글 있을 때: 접혔을 때  */}
						{/* 답글 있을 때: 펼쳤을 때  */}
						{/* 내가 쓴 글인 경우 */}
						{/* 내가 쓴 글인 경우: 수정할 때  */}
						{/* 대댓글 기본  */}
						{/* 대댓글 기본: 내가 쓴 경우 */}
						{/* 대댓글 기본: 내가 쓴 경우: 수정할 때 */}
					</ul>
				</div>
			) : null}
		</div>
	);
};
export default BoardComment;
