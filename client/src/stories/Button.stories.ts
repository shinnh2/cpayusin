import type { Meta, StoryObj } from "@storybook/react";

import Button from "./../components/Button";
import type { ButtonProps } from "./../components/Button";

// More on how to set up stories at: https://storybook.js.org/docs/react/writing-stories/introduction#default-export
const meta = {
	title: "Component/Button",
	component: Button,
	parameters: {
		// Optional parameter to center the component in the Canvas. More info: https://storybook.js.org/docs/react/configure/story-layout
		layout: "centered",
	},
	// This component will have an automatically generated Autodocs entry: https://storybook.js.org/docs/react/writing-docs/autodocs
	tags: ["autodocs"],
	// More on argTypes: https://storybook.js.org/docs/react/api/argtypes
	argTypes: {
		buttonType: {
			control: {
				type: "select", // 컨트롤 유형 (예: select, color, radio 등)
				options: ["primary", "em", "cancel", "another", "error", "no_em"],
				description: "버튼 타입을 나타냅니다.",
			},
		},
		buttonSize: {
			control: {
				type: "select",
				options: ["big", "small"],
				description: "버튼 사이즈를 나타냅니다. 보통 big을 사용합니다.",
			},
		},
		buttonLabel: {
			control: "text", // 텍스트 입력으로 컨트롤
			defaultValue: "테스트",
			description: "원하는 텍스트를 입력해보세요",
		},
		onClick: {
			action: "clicked", // 이벤트 발생 액션 표시
		},
	},
} satisfies Meta<ButtonProps>;

export default meta;
// type Story = StoryObj<typeof meta>;

// More on writing stories with args: https://storybook.js.org/docs/react/writing-stories/args
export const PrimaryBig: Meta<ButtonProps> = {
	args: {
		buttonType: "primary",
		buttonSize: "big",
		buttonLabel: "기본 큰 버튼",
	},
};

export const PrimarySmall: Meta<ButtonProps> = {
	args: {
		buttonType: "primary",
		buttonSize: "small",
		buttonLabel: "기본 작은 버튼",
	},
};

export const Em: Meta<ButtonProps> = {
	args: {
		buttonType: "em",
		buttonSize: "big",
		buttonLabel: "강조 버튼",
	},
};

export const Cancel: Meta<ButtonProps> = {
	args: {
		buttonType: "cancel",
		buttonSize: "big",
		buttonLabel: "취소 버튼",
	},
};

export const Another: Meta<ButtonProps> = {
	args: {
		buttonType: "another",
		buttonSize: "big",
		buttonLabel: "기본과 동등한 다른 버튼",
	},
};

export const Error: Meta<ButtonProps> = {
	args: {
		buttonType: "error",
		buttonSize: "big",
		buttonLabel: "에러 버튼",
	},
};

export const NoEm: Meta<ButtonProps> = {
	args: {
		buttonType: "no_em",
		buttonSize: "big",
		buttonLabel: "강조하지 않는 다른 버튼",
	},
};

// export const Secondary: ButtonProps = {
// 	args: {
// 		label: "Button",
// 	},
// };

// export const Large: ButtonProps = {
// 	args: {
// 		size: "large",
// 		label: "Button",
// 	},
// };

// export const Small: ButtonProps = {
// 	args: {
// 		size: "small",
// 		label: "Button",
// 	},
// };
