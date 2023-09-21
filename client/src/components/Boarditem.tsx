interface Data {
	id?: number;
	title: string;
	content: string;
	files?: object;
	voteCount: number;
	voteStatus?: boolean;
	member: Member;
	createdAt: string;
	modifiedAt?: string;
}
interface Member {
	id?: number;
	nickname: string;
}

const BoardItem = ({ data }: { data: Data }) => {
	return (
		<div className="board_item">
			<div className="board_item_element_wrap">
				<p className="category_tag">카테고리명</p>
				<div className="board_info">
					<p className="info_item comments_info">
						<span className="icon">댓글수</span>
						<span className="info">12</span>
					</p>
					<p className="info_item votes_info">
						<span className="icon">득표수</span>
						<span className="info">{data.voteCount}</span>
					</p>
				</div>
			</div>
			<h3 className="title">{data.title}</h3>
			<div className="board_item_element_wrap">
				<p className="board_date">2023.9.30</p>
				<p className="board_writer">{data.member.nickname}</p>
			</div>
		</div>
	);
};
export default BoardItem;
