// form表单
import type { PlusColumn } from "plus-pro-components";
import { dronesDeviceGetSelectOptions } from "@/api/dronesDevice";

export function useCollectorBusDevForm() {
  const columnsForm: PlusColumn[] = [
    {
      label: "任务名称",
      prop: "taskName",
      valueType: "copy"
    },
    {
      label: "设备信息",
      prop: "deviceId",
      valueType: "select",
      options: async () => {
        return new Promise(resolve => {
          resolve(dronesDeviceGetSelectOptions().then(res => res.data));
        });
      }
    },
    {
      label: "任务描述",
      prop: "taskDescription",
      valueType: "textarea"
    }
  ];

  const columnsQueryForm: PlusColumn[] = [
    {
      label: "任务名称",
      prop: "taskName",
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
