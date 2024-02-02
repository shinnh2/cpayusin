// import { ReactComponent as iconMenu } from "./../assets/icon _menu_.svg";
import { useState, Dispatch, useEffect } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import iconMenu from "./../assets/icon_menu.svg";
import iconWrite from "./../assets/edit_document.svg";
import iconLogout from "./../assets/logout.svg";
import { getAccessToken, removeAccessToken } from "../assets/tokenActions";
import axios from "axios";

export interface HeaderProps {
	isLogin: boolean;
	setIsLogin: Dispatch<React.SetStateAction<boolean>>;
}
interface userData {
	createdAt: string;
	email: string;
	id: number;
	nickname: string;
	profileImage: string | null;
	score: number;
	timeInfo: string;
}
const Header = (props: HeaderProps) => {
	const api = process.env.REACT_APP_API_URL;
	const navigate = useNavigate();
	const [userData, setUserData] = useState<userData>();
	useEffect(() => {
		if (getAccessToken()) {
			let token = getAccessToken();
			axios
				.get(`${api}/api/v1/member/single-info`, {
					headers: { Authorization: token },
				})
				.then((res) => {
					setUserData(res.data.data);
					props.setIsLogin(true);
				})
				.catch((error) => {
					console.error("에러", error);
					console.log("토큰이 인증되지 않았습니다.");
					props.setIsLogin(false);
				});
		} else props.setIsLogin(false);
	}, []);
	const handleLogoutClick = () => {
		removeAccessToken();
		props.setIsLogin(false);
		alert("로그아웃되었습니다.");
		navigate("/");
	};
	return (
		<header className="header">
			<button className="button_navi">
				<img src={iconMenu} alt="메뉴 버튼 이미지" />
			</button>
			<h1 className="logo">JB account</h1>
			<div className="header_button_wrap">
				{props.isLogin ? (
					<>
						<a
							href={`/user/${userData?.id}`}
							className="header_btns link_profile"
							title="클릭시 내 프로필로 이동합니다."
						>
							프로필
						</a>
						<a
							href="/board/write"
							className="header_btns link_write"
							title="클릭시 글쓰기 페이지로 이동합니다."
						>
							<img src={iconWrite} />
						</a>
						<button
							className="header_btns button_logout"
							onClick={handleLogoutClick}
						>
							<img src={iconLogout} />
						</button>
					</>
				) : (
					<>
						<NavLink to="/login" className="header_links login">
							로그인
						</NavLink>
						<NavLink to="/signup" className="header_links signup">
							회원가입
						</NavLink>
					</>
				)}
			</div>
		</header>
	);
};
export default Header;
