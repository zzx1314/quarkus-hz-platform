// form表单
import type { PlusColumn } from "plus-pro-components";

export function useDocForm() {
  const columnsKbForm: PlusColumn[] = [
    {
      label: "相似度阈值",
      tooltip: "相似度阈值，范围10-100",
      prop: "acquaintanceLevel",
      valueType: "input-number",
      fieldProps: { max: 100, min: 10 },
      formItemProps: {
        labelWidth: "115px"
      }
    },
    {
      label: "命中方式",
      prop: "hitHandle",
      valueType: "select",
      options: [
        {
          label: "直接回复",
          value: "直接回复"
        },
        {
          label: "大模型优化",
          value: "大模型优化"
        }
      ],
      formItemProps: {
        labelWidth: "100px",
        style: {
          width: "100%"
        }
      }
    }
  ];
  const columnsForm: PlusColumn[] = [
    {
      label: "分段策略",
      prop: "splitterStrategy",
      valueType: "select",
      options: [
        {
          label: "智能分段",
          value: "智能分段"
        },
        {
          label: "标题分段",
          value: "标题分段"
        },
        {
          label: "全文分段",
          value: "全文分段"
        }
      ],
      formItemProps: {
        labelWidth: "100px",
        style: {
          width: "100%"
        }
      }
    },
    {
      label: "分段长度",
      prop: "splitterLength",
      valueType: "input-number",
      fieldProps: { max: 1000, min: 100 },
      formItemProps: {
        labelWidth: "100px"
      }
    },
    {
      label: "分段标识",
      prop: "splitterFlag",
      valueType: "select",
      options: [
        {
          label: "换行",
          value: "换行"
        },
        {
          label: "分号",
          value: "分号"
        }
      ],
      formItemProps: {
        labelWidth: "100px",
        style: {
          width: "100%"
        }
      }
    }
  ];

  const columnsQueryForm: PlusColumn[] = [
    {
      label: "文档名称",
      prop: "docName",
      valueType: "copy",
      colProps: {
        span: 6
      },
      formItemProps: {
        labelWidth: "100px",
        style: {
          width: "100%"
        }
      }
    },
    {
      label: "文档状态",
      prop: "docName",
      valueType: "copy",
      colProps: {
        span: 6
      },
      formItemProps: {
        labelWidth: "100px",
        style: {
          width: "100%"
        }
      }
    },
    {
      label: "开始时间",
      prop: "beginTime",
      valueType: "date-picker",
      type: "date",
      formItemProps: {
        style: {
          width: "100%"
        }
      },
      fieldProps: {
        type: "date",
        valueFormat: "YYYY-MM-DD HH:mm:ss"
      },
      colProps: {
        span: 6
      }
    },
    {
      label: "结束时间",
      prop: "endTime",
      valueType: "date-picker",
      formItemProps: {
        style: {
          width: "100%"
        }
      },
      fieldProps: {
        type: "date",
        valueFormat: "YYYY-MM-DD"
      },
      colProps: {
        span: 6
      }
    }
  ];
  return {
    columnsKbForm,
    columnsForm,
    columnsQueryForm
  };
}
