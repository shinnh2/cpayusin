import Button from "../components/Button";
import BoardItem from "../components/Boarditem";
import CommentItem from "../components/CommentItem";
import { useState } from "react";

const dummyData = {
	id: 53,
	title: "JPA란",
	content: "~~~~~~",
	files: [
		{
			originalFileName: "orca.jfif",
			storedFileName: "ddfd5ebb-c490-4b9c-bdc0-75c2ccc9e437.jfif",
			url: "https://jbaccount.s3.ap-northeast-2.amazonaws.com/post/ddfd5ebb-c490-4b9c-bdc0-75c2ccc9e437.jfif",
		},
	],
	voteCount: 0,
	voteStatus: false,
	member: {
		id: 1,
		nickname: "운영자",
	},
	createdAt: "2023-09-07T18:48:14.6994829",
	modifiedAt: "2023-09-07T18:48:14.6994829",
};

const UserPage = () => {
	const [tabIndex, setTabIndex] = useState(0);
	const tabMenu = ["작성글", "댓글"];
	const handleclickTabMenuButton = (tabIndex: number) => {
		setTabIndex(tabIndex);
	};

	return (
		<div className="user_page_wrap">
			<div className="user_profile">
				<div className="profile_img_wrap"></div>
				<div className="user_info_wrap">
					<p className="title_h4">User 닉네임</p>
					<p className="email">jbaccount@gmail.com</p>
				</div>
				<div className="user_button_wrap">
					<Button
						buttonType="another"
						buttonSize="big"
						buttonLabel="사용자 정보 수정"
					/>
					<Button
						buttonType="another"
						buttonSize="big"
						buttonLabel="관리자 페이지"
					/>
				</div>
			</div>
			<div className="user_written_wrap">
				<ul className="tab_menu">
					{tabMenu.map((el, idx) => {
						return (
							<li
								key={idx}
								className={
									tabIndex === idx ? "tab_button active" : "tab_button"
								}
								onClick={() => handleclickTabMenuButton(idx)}
							>
								{el}
							</li>
						);
					})}
				</ul>
				<div className="tab_content">
					{/* 작성글인 경우 */}
					{tabIndex === 0 ? (
						<ul className="board">
							<li>
								<BoardItem data={dummyData} />
							</li>
							<li>
								<BoardItem data={dummyData} />
							</li>
						</ul>
					) : null}
					{/* 댓글인 경우 */}
					{tabIndex === 1 ? (
						<ul className="comment">
							<li>
								<CommentItem />
							</li>
							<li>
								<CommentItem />
							</li>
						</ul>
					) : null}
				</div>
			</div>
		</div>
	);
};
export default UserPage;
