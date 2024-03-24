export const isAccessToken = () => {
	const token = localStorage.getItem("accessToken");
	return !!token;
};
export const getAccessToken = () => {
	return localStorage.getItem("accessToken");
};
export const saveAccessToken = (token: string) => {
	localStorage.setItem("accessToken", token);
};
export const removeAccessToken = () => {
	localStorage.removeItem("accessToken");
};
