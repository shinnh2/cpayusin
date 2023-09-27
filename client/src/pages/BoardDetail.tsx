import Button from "../components/Button";

const BoardDetail = () => {
	return (
		<div className="board_box board_detail_box">
			<div className="board_header">
				<h3 className="category_tag">카테고리명</h3>
				<p className="board_date">2023.9.30</p>
				<h4 className="title_h1">게시판 제목</h4>
				<div className="board_detail_info">
					<div className="writer_profile">
						<div className="profile_img_wrap"></div>
						<p className="board_writer">JBAdmin</p>
					</div>
					<div className="info_item votes_info">
						<input type="checkbox" id="board_vote" className="icon" />
						<label htmlFor="board_vote" className="info">
							12
						</label>
					</div>
				</div>
				<div className="writer_action">
					<a href="" className="link">
						수정
					</a>
					<a href="" className="link">
						삭제
					</a>
				</div>
			</div>
			<div className="board_content">내용이 들어올 예정</div>
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
		</div>
	);
};
export default BoardDetail;
