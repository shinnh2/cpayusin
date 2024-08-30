import { useEffect, useState } from "react";
import Input from "./../components/Input";
import Button from "./../components/Button";
import { validator, ValidatorStatus } from "../assets/validater";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const Signup = ({
	isEmailCheck,
	validatedEmail,
}: {
	isEmailCheck: boolean;
	validatedEmail: string;
}) => {
	const api = process.env.REACT_APP_API_URL;
	const navigate = useNavigate();
	const [form, setForm] = useState({
		email: validatedEmail,
		password: "",
		nickname: "",
	});
	const [isError, setIsError] = useState({
		email: false,
		password: false,
		nickname: false,
	});

	useEffect(() => {
		if (!isEmailCheck || !validatedEmail) {
			alert("이메일 인증이 필요합니다.");
			navigate("/validateEmail");
		}
	}, []);

	const setPasswordValue = (value: string) => {
		setForm((prevState) => ({
			...prevState,
			password: value,
		}));
	};
	const setNicknameValue = (value: string) => {
		setForm((prevState) => ({
			...prevState,
			nickname: value,
		}));
	};

	//닉네임 중복 검사
	const handleClickVerifyName = () => {
		const verifyNameForm = {
			nickname: form.nickname,
		};
		axios
			.post(`${api}/api/v1/member/verify-nickname`, verifyNameForm)
			.then((response) => {
				alert("사용 가능한 닉네임입니다.");
			})
			.catch((error) => {
				alert("사용할 수 없는 닉네임입니다.");
			});
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
	const validatorStatusNickname: ValidatorStatus = {
		value: form.nickname,
		isRequired: true,
		valueType: "nickname",
	};

	const handleSubmit = () => {
		setIsError((prevState) => ({
			...prevState,
			email: !validator(validatorStatusEmail),
			password: !validator(validatorStatusPassword),
			nickname: !validator(validatorStatusNickname),
		}));
		if (isError.email || isError.password || isError.nickname) {
			return;
		}

		axios
			.post(`${api}/api/v1/sign-up`, form, { withCredentials: true })
			.then((response) => {
				navigate("/login");
			})
			.catch((error) => {
				if (
					error.response.valueErrors === undefined ||
					error.response.valueErrors.length
				) {
					alert("회원가입에 실패했습니다."); //팝업으로 바꾸기
				} else {
					alert(error.response.valueErrors[0].reason);
				}
			});
	};
	return (
		<div className="input_box sign_box">
			<h3 className="title_h3">회원가입</h3>
			<div className="validate_email_wrap">
				<Input
					InputLabel="이메일"
					isLabel={true}
					errorMsg="올바른 이메일을 입력해 주세요."
					inputAttr={{ type: "text", placeholder: "이메일을 입력하세요" }}
					inputValue={validatedEmail}
				/>
				<Button
					buttonType="primary"
					buttonSize="big"
					buttonLabel="이메일 인증"
				/>
				<p className="validate_email_msg">이메일 인증이 완료되었습니다.</p>
				{/* <p className="validate_email_msg error">이미 가입한 이메일입니다.</p> */}
			</div>
			<div className="content">
				<Input
					InputLabel="비밀번호 입력"
					isLabel={true}
					errorMsg="비밀번호는 8~20자의 영문, 숫자가 포함되어야 합니다. "
					inputAttr={{ type: "password", placeholder: "비밀번호를 입력하세요" }}
					setInputValue={setPasswordValue}
					inputValue={form.password}
					isError={isError.password}
				/>
				<Input
					InputLabel="비밀번호 확인"
					isLabel={true}
					errorMsg="비밀번호가 일치하지 않습니다."
					inputAttr={{
						type: "password",
						placeholder: "비밀번호를 한 번 더 입력하세요",
					}}
				/>
				<Input
					InputLabel="닉네임"
					isLabel={true}
					errorMsg="공백 포함 2~10자 사이의 영문, 한글, 숫자만 가능합니다."
					inputAttr={{
						type: "text",
						placeholder: "닉네임을 입력하세요",
					}}
					setInputValue={setNicknameValue}
					inputValue={form.nickname}
					isError={isError.nickname}
				>
					<Button
						buttonType="another"
						buttonSize="big"
						buttonLabel="중복 확인"
						onClick={handleClickVerifyName}
					/>
				</Input>
				<Button
					buttonType="primary"
					buttonSize="big"
					buttonLabel="회원가입"
					onClick={handleSubmit}
				/>
			</div>
		</div>
	);
};
export default Signup;
