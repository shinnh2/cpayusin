import React, { useState, Dispatch, SetStateAction } from "react";
import Input from "./../components/Input";
import Button from "./../components/Button";
import { validator, ValidatorStatus } from "../assets/validater";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { saveAccessToken } from "../assets/tokenActions";

interface Props {
	setIsLogin: Dispatch<React.SetStateAction<boolean>>;
}

const Login = (props: Props) => {
	const api = process.env.REACT_APP_API_URL;
	const navigate = useNavigate();
	const [form, setForm] = useState({
		email: "",
		password: "",
	});
	const [isError, setIsError] = useState({
		email: false,
		password: false,
	});
	const setEmailValue = (value: string) => {
		setForm((prevState) => ({
			...prevState,
			email: value,
		}));
	};
	const setPasswordValue = (value: string) => {
		setForm((prevState) => ({
			...prevState,
			password: value,
		}));
	};
	//유효성 검사
	const validatorStatusEmail: ValidatorStatus = {
		value: form.email,
		isRequired: true,
		valueType: "email",
	};
	const validatorStatusPassword: ValidatorStatus = {
		value: form.password,
		isRequired: true,
		valueType: "password",
	};
	const handleSubmit = () => {
		setIsError((prevState) => ({
			...prevState,
			email: !validator(validatorStatusEmail),
			password: !validator(validatorStatusPassword),
		}));
		if (isError.email || isError.password) return;
		axios
			.post(`${api}/api/v1/login`, form, { withCredentials: true })
			.then((response) => {
				saveAccessToken(response.headers.authorization);
				props.setIsLogin(true);
				navigate("/");
			})
			.catch((error) => {
				console.error("에러", error);
				alert("로그인에 실패했습니다."); //수정 필요
			});
	};

	return (
		<div className="input_box login_box col_4">
			<h3 className="title_h3">로그인</h3>
			<div className="content">
				<Input
					InputLabel="이메일"
					isLabel={true}
					errorMsg="올바른 이메일을 입력해 주세요."
					inputAttr={{ type: "text", placeholder: "이메일을 입력하세요" }}
					setInputValue={setEmailValue}
					inputValue={form.email}
					isError={isError.email}
				/>
				<Input
					InputLabel="비밀번호"
					isLabel={true}
					errorMsg="올바른 비밀번호를 입력해 주세요."
					inputAttr={{ type: "password", placeholder: "비밀번호를 입력하세요" }}
					setInputValue={setPasswordValue}
					inputValue={form.password}
					isError={isError.password}
				/>
				<Button
					buttonType="primary"
					buttonSize="big"
					buttonLabel="로그인"
					onClick={handleSubmit}
				/>
				<div className="sns_login_wrap">
					<h4 className="sns_login_title">소셜 로그인</h4>
					<p className="sns_login_description">
						별도의 회원가입없이 기존에 가입된 다른 소셜 계정으로 로그인이
						가능합니다.
					</p>
				</div>
				<div className="sns_login_btns">
					<button className="sns_btn kakao">카카오 로그인</button>
					<button className="sns_btn naver">네이버 로그인</button>
					<button className="sns_btn google">구글 로그인</button>
				</div>
			</div>
		</div>
	);
};
export default Login;
