import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import Button from "../components/Button";
import axios from "axios";
import { getAccessToken } from "../assets/tokenActions";

interface BoardDetailData {
	postId: number;
	boardId: number;
	categoryId?: number;
	memberId: number;
	nickname: string;
	title: string;
	content: string;
	files?: any[];
	createdAt: string;
	voteCount: number;
	voteStatus: boolean;
}

const BoardDetail = () => {
	const api = process.env.REACT_APP_API_URL;
	const params = useParams();
	const [boardId, boardName] = params.boardInfo!.split("-");
	const [postData, setPostData] = useState<BoardDetailData>();
	const [myMemberId, setmyMemberID] = useState<number>();
	useEffect(() => {
		const accessToken = getAccessToken();
		axios
			.get(`${api}/api/v1/post/${params.postId}`)
			.then((response) => {
				setPostData(response.data.data);
			})
			.catch((error) => {
				console.error("에러", error);
			});

		if (accessToken) {
			axios
				.get(`${api}/api/v1/member/single-info`, {
					headers: { Authorization: accessToken },
				})
				.then((res) => {
					setmyMemberID(res.data.data.id);
				})
				.catch((_) => {});
		}
	}, []);

	return (
		<div className="board_box board_detail_box">
			{postData === undefined ? (
				"게시글을 불러오지 못했습니다."
			) : (
				<>
					{" "}
					<div className="board_header">
						<p className="board_date">{postData.createdAt}</p>
						<h4 className="title_h1">{postData.title}</h4>
						<div className="board_detail_info">
							<p className="board_info">{params.boardInfo?.split("-")[1]}</p>
							{postData!.categoryId ? (
								<p className="board_info">{postData!.categoryId}</p>
							) : null}
						</div>
						<div className="board_detail_info">
							<div className="writer_profile">
								<div className="profile_img_wrap"></div>
								<p className="board_writer">{postData.nickname}</p>
							</div>
							<div className="info_item votes_info">
								<input type="checkbox" id="board_vote" className="icon" />
								<label htmlFor="board_vote" className="info">
									12
								</label>
							</div>
						</div>
						{myMemberId === postData.memberId ? (
							<div className="writer_action">
								<a
									href={`/board/edit/${boardId}-${boardName}/${params.postId}`}
									className="link"
								>
									수정
								</a>
								<a href="" className="link">
									삭제
								</a>
							</div>
						) : null}
					</div>
					<div
						className="board_content"
						dangerouslySetInnerHTML={{ __html: postData.content }}
					></div>
					<div className="board_comment">
						<div className="comment_write">
							<dl>
								<dt className="title_h4">댓글 작성</dt>
								<dd>
									<textarea placeholder="댓글을 작성하세요"></textarea>
								</dd>
							</dl>
							<Button
								buttonType="primary"
								buttonSize="big"
								buttonLabel="댓글 입력"
							/>
						</div>
						<div className="comment_list">
							{/* 기본  */}
							{/* 답글 있을 때: 접혔을 때  */}
							{/* 답글 있을 때: 펼쳤을 때  */}
							{/* 내가 쓴 글인 경우 */}
							{/* 내가 쓴 글인 경우: 수정할 때  */}
							{/* 대댓글 기본  */}
							{/* 대댓글 기본: 내가 쓴 경우 */}
							{/* 대댓글 기본: 내가 쓴 경우: 수정할 때 */}
						</div>
					</div>
				</>
			)}
		</div>
	);
};
export default BoardDetail;
