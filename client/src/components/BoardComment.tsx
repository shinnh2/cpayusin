import { useState, useEffect } from "react";
import Button from "../components/Button";
import CommentListItem from "./CommentListItem";
import axios from "axios";
import { getAccessToken } from "../assets/tokenActions";
import dummyCommentsData from "./../data/boardCommentsData.json";
import Loading from "./Loading";

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
	const [isLoading, setIsLoading] = useState(true);

	const fetchCommentData = () => {
		const accessToken = getAccessToken();
		if (accessToken) {
			axios
				.get(`${api}/api/v1/comment?postId=${postId}`)
				.then((res) => {
					// console.log(res.data.data); 성공적으로 삭제해도 isRemoved 떠서 확인필요
					setCommentList(res.data.data.comments);
					setIsLoading(false);
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
			{isLoading ? (
				<Loading />
			) : (
				<div className="comments_list_wrap">
					<h4 className="comments_title">댓글 목록</h4>
					{commentList.length > 0 ? (
						<ul className="comment_list">
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
														<li className="comment_list_item" key={childIdx}>
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
						</ul>
					) : (
						"댓글이 없습니다."
					)}
				</div>
			)}
		</div>
	);
};
export default BoardComment;
