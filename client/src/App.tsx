import React, { useState, useEffect } from "react";
import axios from "axios";
import "./App.css";
import Header from "./components/Header";
import Nav from "./components/Nav";
import NavDrawer from "./components/NavDrawer";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import ValidateEmail from "./pages/ValidateEmail";
import NewPassword from "./pages/NewPassword";
import BoardList from "./pages/BoardList";
import BoardDetail from "./pages/BoardDetail";
import BoardWrite from "./pages/BoardWrite";
import UserPage from "./pages/UserPage";
import UserEdit from "./pages/UserEdit";
import BoardDetailEdit from "./pages/BoardDetailEdit";
import Admin from "./pages/Admin";
import Loading from "./components/Loading";

export interface userDataType {
	createdAt: string;
	email: string;
	id: number;
	nickname: string;
	profileImage: string | null;
	score: number;
	role: string;
	timeInfo?: string;
}

function App() {
	const api = process.env.REACT_APP_API_URL;
	const [isLoading, setIsLoading] = useState(true);
	const [isLogin, setIsLogin] = useState(false);
	const [userData, setUserData] = useState<userDataType>();
	const [isEmailCheck, setIsEmailCheck] = useState(false);
	const [validatedEmail, setValidatedEmail] = useState("");
	const [isNavDrawerOn, setIsNavDrawerOn] = useState(false);
	const [menuData, setMenuData] = useState<any[]>([]);

	const fetchMenuData = async () => {
		try {
			const response = await axios.get(`${api}/api/v1/board/menu`);
			setMenuData(response.data.data); // 데이터가 있을 경우 setMenuData에 설정
		} catch (error) {
			console.error("에러", error);
			setMenuData([]); // 데이터가 없을 경우 빈 배열로 설정 (로딩이 끝나게 하기 위함)
		} finally {
			setIsLoading(false); // 로딩 상태 종료
		}
	};

	useEffect(() => {
		setIsLoading(true);
		fetchMenuData();
	}, []);

	return (
		<div className="App">
			<Router>
				<Header
					isLogin={isLogin}
					setIsLogin={setIsLogin}
					setUserData={setUserData}
					userData={userData}
					setIsNavDrawerOn={setIsNavDrawerOn}
				/>
				<div className="page_wrap">
					<Nav menuData={menuData} setIsNavDrawerOn={setIsNavDrawerOn} />
					<main className="container">
						{isLoading || !menuData ? (
							<Loading />
						) : (
							<Routes>
								<Route path="/" element={<Home />} />
								<Route
									path="/login"
									element={<Login setIsLogin={setIsLogin} />}
								/>
								<Route
									path="/signup"
									element={
										<Signup
											isEmailCheck={isEmailCheck}
											validatedEmail={validatedEmail}
										/>
									}
								/>
								<Route
									path="/validateEmail"
									element={
										<ValidateEmail
											setIsEmailCheck={setIsEmailCheck}
											setValidatedEmail={setValidatedEmail}
											isEmailCheck={isEmailCheck}
											validatedEmail={validatedEmail}
										/>
									}
								/>
								<Route path="/newPassword" element={<NewPassword />} />
								<Route
									path="/board/:boardInfo"
									element={<BoardList menuData={menuData} />}
								/>
								<Route
									path="/:postId"
									element={<BoardDetail menuData={menuData} />}
								/>
								<Route path="/board/write" element={<BoardWrite />} />
								<Route
									path="/board/edit/:postId"
									element={<BoardDetailEdit />}
								/>
								<Route
									path="/user/:userId"
									element={<UserPage userData={userData} />}
								/>
								<Route
									path="/user/:userId/edit"
									element={<UserEdit userData={userData} />}
								/>
								<Route path="/admin" element={<Admin />} />
							</Routes>
						)}
					</main>
				</div>
				<NavDrawer
					isLogin={isLogin}
					setIsLogin={setIsLogin}
					userData={userData}
					setIsNavDrawerOn={setIsNavDrawerOn}
					isNavDrawerOn={isNavDrawerOn}
					menuData={menuData}
				/>
			</Router>
		</div>
	);
}

export default App;
