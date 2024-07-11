export interface ValidatorStatus {
	value: number | string; //값
	isRequired: boolean; //필수 여부
	valueType: "email" | "password" | "nickname"; //값 종류
}

export function validator(validateStatus: ValidatorStatus): boolean {
	let isValid = true;

	//필수 여부 확인
	const target = validateStatus.value.toString();
	if (validateStatus.isRequired) {
		isValid = isValid && target.trim().length !== 0;
	}
	//주어진 값의 종류 판별 후 검사
	let reg: RegExp;
	switch (validateStatus.valueType) {
		case "email":
			reg = /^[a-zA-Z0-9+-\_.]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/;
			break;
		case "password":
			reg = /(?=.*\d)(?=.*[a-zA-Z]).{8,20}/;
			break;
		case "nickname":
			reg = /(?=.*\d)|(?=.*[a-zA-Z])|(?=.*[가-힣]).{2,8}/;
			break;
	}
	isValid = isValid && reg.test(target);
	return isValid;
}
