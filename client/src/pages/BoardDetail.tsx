import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import { getAccessToken } from "../assets/tokenActions";
import BoardComment from "../components/BoardComment";
import VoteInfo from "../components/VoteInfo";

interface BoardDetailData {
	postId: number;
	boardId: number;
	boardName: string;
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

const BoardDetail = ({ menuData }: { menuData: any[] }) => {
	const api = process.env.REACT_APP_API_URL;
	const params = useParams();
	const navigate = useNavigate();
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
				.get(`${api}/api/v1/member/profile`, {
					headers: { Authorization: accessToken },
				})
				.then((res) => {
					setmyMemberID(res.data.data.id);
				})
				.catch((_) => {});
		}
	}, []);

	const handleClickDeletePost = (
		event: React.MouseEvent<HTMLAnchorElement>
	) => {
		event.preventDefault();
		const accessToken = getAccessToken();
		//팝업창으로 대체할 것
		let isDeleteYes = window.confirm("게시물을 삭제하시겠습니까?");
		if (isDeleteYes) {
			console.log(params.postId);
			axios
				.delete(`${api}/api/v1/post/delete/${params.postId}`, {
					headers: { Authorization: accessToken },
				})
				.then((res) => {
					navigate(`/board/${postData!.boardName}`);
				})
				.catch((error) => {
					alert("게시물 삭제를 실패했습니다.");
				});
		}
	};

	return (
		<div className="board_box board_detail_box">
			{postData === undefined ? (
				"게시글을 불러오지 못했습니다."
			) : (
				<>
					<div className="board_header">
						<div className="board_detail_info">
							<p className="board_info">{postData!.boardName}</p>
							{postData!.categoryId ? (
								<p className="board_info">{postData!.categoryId}</p>
							) : null}
							<p className="board_date">{postData.createdAt}</p>
						</div>
						<h4 className="board_detail_title">{postData.title}</h4>
						<div className="board_detail_info">
							<div className="writer_profile">
								<div className="profile_img_wrap"></div>
								<p className="board_writer">{postData.nickname}</p>
							</div>
							<VoteInfo
								voteCount={postData.voteCount}
								isVoted={postData.voteStatus}
								endpoint={`/api/v1/vote/post/${postData.postId}`}
							/>
						</div>
						{myMemberId === postData.memberId ? (
							<div className="writer_action">
								<a href={`/board/edit/${params.postId}`} className="link">
									수정
								</a>
								<a href="" className="link" onClick={handleClickDeletePost}>
									삭제
								</a>
							</div>
						) : null}
					</div>
					<div
						className="board_content"
						dangerouslySetInnerHTML={{ __html: postData.content }}
					></div>
					<BoardComment postId={params.postId!} memberId={myMemberId!} />
				</>
			)}
		</div>
	);
};
export default BoardDetail;
