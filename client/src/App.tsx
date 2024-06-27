import React, { useState } from "react";
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
	const [isLogin, setIsLogin] = useState(false);
	const [userData, setUserData] = useState<userDataType>();
	const [isNavDrawerOn, setIsNavDrawerOn] = useState(false);
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
					<Nav />
					<main className="container">
						<Routes>
							<Route path="/" element={<Home />} />
							<Route
								path="/login"
								element={<Login setIsLogin={setIsLogin} />}
							/>
							<Route path="/signup" element={<Signup />} />
							<Route path="/validateEmail" element={<ValidateEmail />} />
							<Route path="/newPassword" element={<NewPassword />} />
							<Route path="/board/:boardInfo" element={<BoardList />} />
							<Route
								path="/board/:boardInfo/:postId"
								element={<BoardDetail />}
							/>
							<Route path="/board/write" element={<BoardWrite />} />
							<Route
								path="/board/edit/:boardInfo/:postId"
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
					</main>
				</div>
				<NavDrawer
					isLogin={isLogin}
					setIsLogin={setIsLogin}
					userData={userData}
					setIsNavDrawerOn={setIsNavDrawerOn}
					isNavDrawerOn={isNavDrawerOn}
				/>
			</Router>
		</div>
	);
}

export default App;
