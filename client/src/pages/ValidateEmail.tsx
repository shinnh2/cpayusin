import { useEffect, useState } from "react";
import Input from "./../components/Input";
import Button from "./../components/Button";
import { validator, ValidatorStatus } from "../assets/validater";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const ValidateEmail = ({
	setIsEmailCheck,
	setValidatedEmail,
	isEmailCheck,
	validatedEmail,
}: {
	setIsEmailCheck: React.Dispatch<React.SetStateAction<boolean>>;
	setValidatedEmail: React.Dispatch<React.SetStateAction<string>>;
	isEmailCheck: boolean;
	validatedEmail: string;
}) => {
	const api = process.env.REACT_APP_API_URL;
	const navigate = useNavigate();
	const [emailValue, setEmailValue] = useState("");
	const [isEmailError, setIsEmailrror] = useState(false);
	const [isValidateError, setIsValidateError] = useState(false);
	const [canInputValidateCode, setCanInputValidateCode] = useState(false);
	const [codeValue, setCodeValue] = useState("");

	const handleInputEmailValue = (value: string) => {
		setEmailValue(value);
	};
	const validatorStatusEmail: ValidatorStatus = {
		value: emailValue,
		isRequired: true,
		valueType: "email",
	};
	const handleClickValidateCode = () => {
		setIsEmailrror(!validator(validatorStatusEmail));
		if (isEmailError) {
			return;
		}

		axios
			.post(`${api}/api/v1/mail/send-verification`, { email: emailValue })
			.then((response) => {
				setIsValidateError(false);
				setCanInputValidateCode(true);
			})
			.catch((error) => {
				setIsValidateError(true);
			});
	};
	const handleInputCode = (value: string) => {
		setCodeValue(value);
	};
	const handleClickSubmitCode = () => {
		const form = {
			email: emailValue,
			verificationCode: codeValue,
		};
		console.log(form);
		axios
			.post(`${api}/api/v1/verification`, form)
			.then((response) => {
				setIsEmailCheck(true);
				setValidatedEmail(emailValue);
				navigate("/signup");
			})
			.catch((error) => {
				console.log("인증코드 입력 에러", error);
				alert("인증코드 입력에 실패했습니다. 다시 시도해 주십시오.");
			});
	};
	useEffect(() => {
		if (isEmailCheck && validatedEmail) {
			navigate("/signup");
		}
	}, [isEmailCheck, validatedEmail]);

	return (
		<div className="input_box validate_email_box col_4">
			<h3 className="title_h3">이메일 인증</h3>
			<div className="validate_email_wrap">
				<Input
					InputLabel="이메일"
					isLabel={true}
					errorMsg="올바른 이메일을 입력하세요."
					inputAttr={{
						type: "text",
						placeholder: "이메일을 입력하세요.",
					}}
					setInputValue={handleInputEmailValue}
					inputValue={emailValue}
					isError={isEmailError}
				/>
				<Button
					buttonType="primary"
					buttonSize="big"
					buttonLabel="인증코드 발급"
					onClick={handleClickValidateCode}
				/>
				{canInputValidateCode ? (
					<p className="validate_email_msg">인증코드가 전송되었습니다.</p>
				) : null}
				{isValidateError ? (
					<p className="validate_email_msg error">
						인증코드 전송에 실패했습니다.
					</p>
				) : null}
			</div>
			{canInputValidateCode ? (
				<div className="content">
					<Input
						InputLabel="인증코드 확인"
						isLabel={true}
						errorMsg="입력하신 인증코드가 올바르지 않습니다."
						inputAttr={{
							type: "text",
							placeholder: "인증코드를 입력하세요",
						}}
						setInputValue={handleInputCode}
						inputValue={codeValue}
					/>
					<Button
						buttonType="primary"
						buttonSize="big"
						buttonLabel="입력완료"
						onClick={handleClickSubmitCode}
					/>
				</div>
			) : null}
		</div>
	);
};
export default ValidateEmail;
