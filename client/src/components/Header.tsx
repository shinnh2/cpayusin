// import { ReactComponent as iconMenu } from "./../assets/icon _menu_.svg";
import { useState } from "react";
import { NavLink } from "react-router-dom";
import iconMenu from "./../assets/icon_menu.svg";
import iconWrite from "./../assets/edit_document.svg";
import iconLogout from "./../assets/logout.svg";

export interface HeaderProps {
	isLogin: boolean;
}
const Header = ({ isLogin }: HeaderProps) => {
	return (
		<header className="header">
			<button className="button_navi">
				<img src={iconMenu} alt="메뉴 버튼 이미지" />
			</button>
			<h1 className="logo">JB account</h1>
			<div className="header_button_wrap">
				{isLogin ? (
					<>
						<a
							href=""
							className="header_btns link_profile"
							title="클릭시 내 프로필로 이동합니다."
						>
							프로필
						</a>
						<a
							href=""
							className="header_btns link_write"
							title="클릭시 글쓰기 페이지로 이동합니다."
						>
							<img src={iconWrite} />
						</a>
						<button className="header_btns button_logout">
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
