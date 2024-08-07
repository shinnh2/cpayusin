import { Dispatch } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { getAccessToken, removeAccessToken } from "../assets/tokenActions";
import iconWrite from "./../assets/edit_document.svg";
import iconLogout from "./../assets/logout.svg";
import iconUser from "./../assets/icon_user.svg";
import { userDataType } from "./../App";

interface LoginInfoProps {
	isDrawer: boolean;
	isLogin: boolean;
	setIsLogin: Dispatch<React.SetStateAction<boolean>>;
	userData: userDataType | undefined;
}

const LoginInfo = (props: LoginInfoProps) => {
	const navigate = useNavigate();
	const handleLogoutClick = () => {
		removeAccessToken();
		props.setIsLogin(false);
		alert("로그아웃되었습니다.");
		navigate("/");
	};
	return (
		<div className="login_info_wrap">
			{props.isLogin ? (
				<>
					<a
						href={`/user/${props.userData?.id}`}
						className="login_info_btns link_profile"
						title="클릭시 내 프로필로 이동합니다."
					>
						<div className="login_info_icon_wrap profile">
							{props.userData?.profileImage ? (
								<img src={props.userData?.profileImage} />
							) : (
								<img
									src={iconUser}
									alt="기본 유저 아이콘"
									className="default"
								/>
							)}
						</div>
						{props.isDrawer ? (
							<span className="login_info_btns_name">내 프로필</span>
						) : null}
					</a>
					<a
						href="/board/write"
						className="login_info_btns link_write"
						title="클릭시 글쓰기 페이지로 이동합니다."
					>
						<div className="login_info_icon_wrap">
							<img src={iconWrite} alt="글쓰기 아이콘" />
						</div>
						{props.isDrawer ? (
							<span className="login_info_btns_name">글쓰기</span>
						) : null}
					</a>
					<button
						className="login_info_btns button_logout"
						onClick={handleLogoutClick}
					>
						<div className="login_info_icon_wrap">
							<img src={iconLogout} alt="로그아웃 아이콘" />
						</div>
						{props.isDrawer ? (
							<span className="login_info_btns_name">로그아웃</span>
						) : null}
					</button>
				</>
			) : (
				<>
					<NavLink to="/login" className="login_info_links login">
						로그인
					</NavLink>
					<NavLink to="/signup" className="login_info_links signup">
						회원가입
					</NavLink>
				</>
			)}
		</div>
	);
};
export default LoginInfo;
