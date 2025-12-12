// form表单
import type { PlusColumn } from "plus-pro-components";

export function useCollectorBusDevForm() {
  const columnsForm: PlusColumn[] = [
    {
      label: "设备ID",
      prop: "deviceId",
      valueType: "copy"
    },
    {
      label: "设备名称",
      prop: "deviceName",
      valueType: "copy"
    },
    {
      label: "设备类型",
      prop: "deviceType",
      valueType: "copy"
    },
    {
      label: "设备序列号",
      prop: "serialNumber",
      valueType: "copy"
    },
    {
      label: "设备状态",
      prop: "status",
      valueType: "copy"
    },
    {
      label: "IP地址",
      prop: "ipAddress",
      valueType: "copy"
    },
    {
      label: "设备位置",
      prop: "location",
      valueType: "copy"
    },
    {
      label: "设备型号",
      prop: "model",
      valueType: "copy"
    },
    {
      label: "固件版本",
      prop: "firmwareVersion",
      valueType: "copy"
    },
    {
      label: "备注",
      prop: "remark",
      valueType: "textarea"
    }
  ];

  const columnsQueryForm: PlusColumn[] = [
    {
      label: "设备ID",
      prop: "deviceId",
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
