import Nav from "./Nav";
import LoginInfo from "./LoginInfo";
import iconClose from "./../assets/icon_close.svg";
import { userDataType } from "./../App";

interface NavDrawerProps {
	isLogin: boolean;
	setIsLogin: React.Dispatch<React.SetStateAction<boolean>>;
	userData: userDataType | undefined;
	setIsNavDrawerOn: React.Dispatch<React.SetStateAction<boolean>>;
	isNavDrawerOn: boolean;
	menuData: any[];
}

const NavDrawer = (props: NavDrawerProps) => {
	const handleClickNavDrawerClose = () => {
		props.setIsNavDrawerOn(false);
	};

	return (
		<div
			className={
				props.isNavDrawerOn ? "nav_drawer_wrap" : "nav_drawer_wrap off"
			}
		>
			<div className="drawer_dim" onClick={handleClickNavDrawerClose}></div>
			<div className="drawer">
				<div className="drawer_head">
					<button
						className="nav_drawer_close"
						onClick={handleClickNavDrawerClose}
					>
						<img src={iconClose} alt="네비게이션 드로어 닫기" />
					</button>
					{props.isLogin ? (
						<div className="drawer_title_wrap">
							<h3 className="drawer_title">{props.userData?.nickname}</h3>
							<p className="drawer_sub_title">{props.userData?.email}</p>
						</div>
					) : (
						<div className="drawer_title_wrap">
							<h3 className="drawer_title">안녕하세요.</h3>
							<p className="drawer_sub_title">로그인이 필요합니다.</p>
						</div>
					)}
					<LoginInfo
						isDrawer={true}
						isLogin={props.isLogin}
						userData={props.userData}
						setIsLogin={props.setIsLogin}
					/>
				</div>

				<Nav menuData={props.menuData} />
			</div>
		</div>
	);
};
export default NavDrawer;
