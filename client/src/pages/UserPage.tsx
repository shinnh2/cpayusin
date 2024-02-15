import Button from "../components/Button";
import CommentItem from "../components/CommentItem";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { isAccessToken } from "../assets/tokenActions";
import { userDataType } from "./../App";
import BoardListPagination from "../components/BoardListPagination";
import iconUser from "./../assets/icon_user.svg";

// interface userDataType {
// 	createdAt: string;
// 	email: string;
// 	id: number;
// 	nickname: string;
// 	profileImage: string | null;
// 	score: number;
//  isAdmin: boolean;
// 	timeInfo: string;
// }

interface userPageProps {
	userData: userDataType | undefined;
}

const UserPage: React.FC<userPageProps> = ({ userData }) => {
	const params = useParams();
	const navigate = useNavigate();
	const api = process.env.REACT_APP_API_URL;
	const [tabIndex, setTabIndex] = useState(0);
	const tabMenu = ["작성글", "댓글"];
	const handleclickTabMenuButton = (tabIndex: number) => {
		setTabIndex(tabIndex);
	};
	useEffect(() => {
		let isToken = isAccessToken();
		if (isToken === false) {
			alert("로그인이 필요한 서비스입니다.");
			navigate(`/login`);
		}
	}, []);

	return (
		<div className="user_page_wrap">
			<div className="user_profile">
				{userData?.profileImage !== null ? null : (
					<div className="profile_img_wrap">
						<img src={iconUser} className="profile_img_default" />
					</div>
				)}

				<div className="user_info_wrap">
					<p className="title_h4">{userData?.nickname}</p>
					<p className="email">{userData?.email}</p>
				</div>
				<div className="user_button_wrap">
					<Button
						buttonType="another"
						buttonSize="big"
						buttonLabel="사용자 정보 수정"
					/>
					{userData?.isAdmin ? (
						<Button
							buttonType="another"
							buttonSize="big"
							buttonLabel="관리자 페이지"
						/>
					) : null}
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
					<div
						className={
							tabIndex === 0 ? "tabContentItem show" : "tabContentItem"
						}
					>
						<BoardListPagination />
					</div>
					{/* 댓글인 경우 */}
					<div
						className={
							tabIndex === 1 ? "tabContentItem show" : "tabContentItem"
						}
					>
						<ul className="comment">
							<li>
								<CommentItem />
							</li>
							<li>
								<CommentItem />
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	);
};
export default UserPage;
