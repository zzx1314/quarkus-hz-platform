// form表单
import type { PlusColumn } from "plus-pro-components";

export function useCollectorBusDevForm() {
  const columnsForm: PlusColumn[] = [
    {
      label: "模型名称",
      prop: "modelName",
      valueType: "copy"
    },
    {
      label: "模型类型",
      prop: "modelType",
      valueType: "select",
      options: [
        {
          label: "chat",
          value: "chat"
        },
        {
          label: "embedding",
          value: "embedding"
        }
      ]
    },
    {
      label: "状态",
      prop: "enable",
      valueType: "select",
      options: [
        {
          label: "启用",
          value: "true"
        },
        {
          label: "禁用",
          value: "false"
        }
      ]
    },
    {
      label: "接口地址",
      prop: "baseUrl",
      valueType: "copy"
    },
    {
      label: "APIKEY",
      prop: "apiKey",
      valueType: "copy"
    },
    {
      label: "温度",
      prop: "temperature",
      tooltip:
        "采样温度，介于 0 和 2 之间。较高的值（如 0.8）将使输出更加随机，而较低的值（如 0.2）将使其更加集中和确定。",
      valueType: "input-number",
      fieldProps: {
        min: 0,
        max: 2,
        step: 0.1
      }
    },
    {
      label: "最大Token数",
      prop: "maxTokens",
      tooltip: "聊天完成时可生成的最大令牌数。",
      valueType: "input-number",
      fieldProps: {
        min: 0,
        max: 2048
      }
    },
    {
      label: "惩罚频率",
      prop: "frequencyPenalty",
      tooltip:
        "介于 -2.0 和 2.0 之间的数字。正值会根据新标记在文本中出现的频率对其进行惩罚，从而降低模型逐字重复同一行内容的可能性。",
      valueType: "input-number",
      fieldProps: {
        min: -2,
        max: 2,
        step: 0.1
      }
    },
    {
      label: "备注",
      prop: "remark",
      valueType: "textarea"
    }
  ];

  const columnsQueryForm: PlusColumn[] = [
    {
      label: "模型名称",
      prop: "modelName",
      valueType: "copy",
      colProps: {
        span: 5
      }
    },
    {
      label: "开始时间",
      prop: "beginTime",
      valueType: "date-picker",
      type: "date",
      formItemProps: {
        style: {
          width: "250px"
        }
      },
      fieldProps: {
        type: "date",
        valueFormat: "YYYY-MM-DD HH:mm:ss"
      },
      colProps: {
        span: 5
      }
    },
    {
      label: "结束时间",
      prop: "endTime",
      valueType: "date-picker",
      formItemProps: {
        style: {
          width: "250px"
        }
      },
      fieldProps: {
        type: "date",
        valueFormat: "YYYY-MM-DD"
      },
      colProps: {
        span: 5
      }
    }
  ];
  return {
    columnsForm,
    columnsQueryForm
  };
}
