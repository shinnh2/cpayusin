// import { ReactComponent as iconMenu } from "./../assets/icon _menu_.svg";
import { useState, Dispatch, useEffect } from "react";
import iconMenu from "./../assets/icon_menu.svg";
import { getAccessToken, removeAccessToken } from "../assets/tokenActions";
import axios from "axios";
import { userDataType } from "./../App";
import LoginInfo from "./LoginInfo";

export interface HeaderProps {
	isLogin: boolean;
	setIsLogin: Dispatch<React.SetStateAction<boolean>>;
	setUserData: Dispatch<React.SetStateAction<userDataType | undefined>>;
	userData: userDataType | undefined;
	setIsNavDrawerOn: any;
}
// interface userData {
// 	createdAt: string;
// 	email: string;
// 	id: number;
// 	nickname: string;
// 	profileImage: string | null;
// 	score: number;
// 	timeInfo: string;
// }
const Header = (props: HeaderProps) => {
	const api = process.env.REACT_APP_API_URL;
	useEffect(() => {
		let token = getAccessToken();
		if (token) {
			axios
				.get(`${api}/api/v1/member/profile`, {
					headers: { Authorization: token },
				})
				.then((res) => {
					props.setUserData(res.data.data);
					props.setIsLogin(true);
				})
				.catch((error) => {
					props.setIsLogin(false);
					removeAccessToken();
				});
		} else props.setIsLogin(false);
	}, []);
	const handleNavDrawerClick = () => {
		props.setIsNavDrawerOn(true);
	};
	return (
		<header className="header">
			<h1>
				<a href="/" className="logo" title="cpayusin 홈">
					<img className="logo_img" src="/images/logo.png" />
				</a>
			</h1>
			<LoginInfo
				isDrawer={false}
				isLogin={props.isLogin}
				userData={props.userData}
				setIsLogin={props.setIsLogin}
			/>
			<button className="button_navi" onClick={handleNavDrawerClick}>
				<img src={iconMenu} alt="메뉴 버튼 이미지" />
			</button>
		</header>
	);
};
export default Header;
