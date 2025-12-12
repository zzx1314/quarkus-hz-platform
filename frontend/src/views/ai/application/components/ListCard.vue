<script setup lang="ts">
import { computed, PropType } from "vue";
import applicationSvg from "@/assets/svg/application.svg?component";
import More2Fill from "~icons/ri/more-2-fill";
import EmojioneMonotoneDirectHit from "~icons/emojione-monotone/direct-hit";
import StreamlineStartup from "~icons/streamline/startup";
import IconDelete from "~icons/ep/delete";
import IconEditPen from "~icons/ep/editPen";
import CarbonIbmEventProcessing from "~icons/carbon/ibm-event-processing";
import { useProcess } from "@/views/ai/application/hooks/processHook";

defineOptions({
  name: "ReCard"
});

const { toProcessDetail } = useProcess();

interface CardApplicationType {
  id: number;
  type: string;
  isSetup: boolean;
  description: string;
  name: string;
  createTime: string;
  createUsername: string;
}

const props = defineProps({
  application: {
    type: Object as PropType<CardApplicationType>
  }
});

const emit = defineEmits([
  "manage-application",
  "delete-item",
  "click-item",
  "enable-item",
  "hit-test"
]);

const handleClickManage = (application: CardApplicationType) => {
  emit("manage-application", application);
};

const handleClickDelete = (application: CardApplicationType) => {
  emit("delete-item", application);
};

const handleClick = (paragraph: CardApplicationType) => {
  emit("click-item", paragraph);
};

const handleClickEnable = (application: CardApplicationType) => {
  emit("enable-item", application);
};

const handleClickHit = (application: CardApplicationType) => {
  emit("hit-test", application);
};

const findProcess = (application: CardApplicationType) => {
  toProcessDetail({
    appId: application.id,
    name: application.name,
    isShowSave: "false"
  });
};

const cardClass = computed(() => [
  "list-card-item",
  { "list-card-item__disabled": !props.application.isSetup }
]);

const cardLogoClass = computed(() => [
  "list-card-item_detail--logo",
  { "list-card-item_detail--logo__disabled": !props.application.isSetup }
]);
</script>

<template>
  <div :class="cardClass">
    <div class="list-card-item_detail bg-bg_color">
      <el-row justify="space-between">
        <div :class="cardLogoClass">
          <applicationSvg />
        </div>
        <div class="list-card-item_detail--operation">
          <el-tag
            :color="application.isSetup ? '#00a870' : '#eee'"
            effect="dark"
            class="mx-1 list-card-item_detail--operation--tag"
          >
            {{ application.isSetup ? "已启用" : "已停用" }}
          </el-tag>
          <el-dropdown trigger="click">
            <IconifyIconOffline :icon="More2Fill" class="text-[24px]" />
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleClickEnable(application)">
                  <streamline-startup />
                  <div style="margin-left: 2px">
                    {{ !application.isSetup ? "启用" : "停用" }}
                  </div>
                </el-dropdown-item>
                <el-dropdown-item @click="handleClickHit(application)">
                  <emojione-monotone-direct-hit />
                  <div style="margin-left: 2px">命中测试</div>
                </el-dropdown-item>
                <el-dropdown-item
                  @click="findProcess(application)"
                  v-if="application.type === '复杂应用'"
                >
                  <carbon-ibm-event-processing />
                  <div style="margin-left: 2px">查看流程</div>
                </el-dropdown-item>
                <el-dropdown-item @click="handleClickManage(application)">
                  <icon-edit-pen />
                  <div style="margin-left: 2px">修改</div>
                </el-dropdown-item>
                <el-dropdown-item @click="handleClickDelete(application)">
                  <icon-delete />
                  <div style="margin-left: 2px">删除</div>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-row>
      <div @click="handleClick(application)">
        <p class="list-card-item_detail--name text-text_color_primary">
          {{ application.name }}
        </p>
        <p class="list-card-item_detail--desc text-text_color_regular">
          {{ application.description }}
        </p>
      </div>
      <div>
        <el-tag
          size="small"
          :type="application.type === '简单应用' ? 'success' : 'primary'"
          >{{ application.type }}</el-tag
        >
        <el-tag
          size="small"
          color="#f3e8ff"
          style="margin-left: 4px; color: #8e44ad"
          >{{ application.createTime }}</el-tag
        >
        <el-tag
          size="small"
          color="#fdf6ec"
          style="margin-left: 4px; color: #e6a23c"
          >{{ application.createUsername }}</el-tag
        >
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.list-card-item {
  display: flex;
  flex-direction: column;
  margin-bottom: 12px;
  overflow: hidden;
  cursor: pointer;
  border-radius: 3px;

  &_detail {
    flex: 1;
    min-height: 140px;
    padding: 10px 8px;

    &--logo {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 46px;
      height: 46px;
      font-size: 26px;
      color: #0052d9;
      background: #e0ebff;
      border-radius: 50%;

      &__disabled {
        color: #a1c4ff;
      }
    }

    &--operation {
      display: flex;
      height: 100%;

      &--tag {
        border: 0;
      }
    }

    &--name {
      margin: 24px 0 8px;
      font-size: 16px;
      font-weight: 400;
    }

    &--desc {
      display: -webkit-box;
      height: 40px;
      margin-bottom: 24px;
      overflow: hidden;
      text-overflow: ellipsis;
      -webkit-line-clamp: 2;
      font-size: 12px;
      line-height: 20px;
      -webkit-box-orient: vertical;
    }
  }

  &__disabled {
    .list-card-item_detail--name,
    .list-card-item_detail--desc {
      color: var(--el-text-color-disabled);
    }

    .list-card-item_detail--operation--tag {
      color: #bababa;
    }
  }
}
</style>
