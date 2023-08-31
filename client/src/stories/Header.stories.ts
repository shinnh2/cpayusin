import type { Meta } from "@storybook/react";

import Header from "./../components/Header";
import { HeaderProps } from "./../components/Header";

const meta = {
	title: "Component/Header",
	component: Header,
	// This component will have an automatically generated Autodocs entry: https://storybook.js.org/docs/react/writing-docs/autodocs
	tags: ["autodocs"],
	parameters: {
		// More on how to position stories at: https://storybook.js.org/docs/react/configure/story-layout
		layout: "fullscreen",
	},
	argTypes: {
		isLogin: {
			control: {
				type: "boolean", //로그인 여부를 나타냅니다.
			},
		},
	},
} satisfies Meta<HeaderProps>;

export default meta;
// type Story = StoryObj<typeof meta>;

export const loginHeader: Meta<HeaderProps> = {
	args: {
		isLogin: true,
	},
};

export const logoutHeader: Meta<HeaderProps> = {
	args: {
		isLogin: false,
	},
};

// export const LoggedIn: Story = {
// 	args: {
// 		user: {
// 			name: "Jane Doe",
// 		},
// 	},
// };

// export const LoggedOut: Story = {};
