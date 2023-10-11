import Input from "./../components/Input";
import Button from "./../components/Button";
import { validator, ValidatorStatus } from "../assets/validater";
import { useState } from "react";

const Login = () => {
	const [value, setValue] = useState({
		email: "",
		password: "",
	});
	const [isError, setIsError] = useState({
		email: false,
		password: false,
	});
	const setEmailValue = (value: string) => {
		setValue((prevState) => ({
			...prevState,
			email: value,
		}));
	};
	const setPasswordValue = (value: string) => {
		setValue((prevState) => ({
			...prevState,
			password: value,
		}));
	};
	//유효성 검사
	const validatorStatusEmail: ValidatorStatus = {
		value: value.email,
		isRequired: true,
		valueType: "email",
	};
	const validatorStatusPassword: ValidatorStatus = {
		value: value.password,
		isRequired: true,
		valueType: "password",
	};
	const handleOnclick = () => {
		setIsError((prevState) => ({
			...prevState,
			email: !validator(validatorStatusEmail),
			password: !validator(validatorStatusPassword),
		}));
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
					inputValue={value.email}
					isError={isError.email}
				/>
				<Input
					InputLabel="비밀번호"
					isLabel={true}
					errorMsg="올바른 비밀번호를 입력해 주세요."
					inputAttr={{ type: "password", placeholder: "비밀번호를 입력하세요" }}
					setInputValue={setPasswordValue}
					inputValue={value.password}
					isError={isError.password}
				/>
				<Button
					buttonType="primary"
					buttonSize="big"
					buttonLabel="로그인"
					onClick={handleOnclick}
				/>
			</div>
		</div>
	);
};
export default Login;
