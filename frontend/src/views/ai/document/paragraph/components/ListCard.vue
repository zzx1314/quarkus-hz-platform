<script setup lang="ts">
import { computed, PropType } from "vue";
import shopIcon from "@/assets/svg/shop.svg?component";
import More2Fill from "~icons/ri/more-2-fill";

defineOptions({
  name: "ReCard"
});

interface CardParagraphType {
  content: string;
  isSetup: boolean;
  characterNumber: number;
}

const props = defineProps({
  paragraph: {
    type: Object as PropType<CardParagraphType>
  }
});

const emit = defineEmits(["manage-paragraph", "delete-item", "click-item"]);

const handleClickManage = (paragraph: CardParagraphType) => {
  emit("manage-paragraph", paragraph);
};

const handleClickDelete = (paragraph: CardParagraphType) => {
  emit("delete-item", paragraph);
};

const cardClass = computed(() => [
  "list-card-item",
  { "list-card-item__disabled": !props.paragraph.isSetup }
]);

const cardLogoClass = computed(() => [
  "list-card-item_detail--logo",
  { "list-card-item_detail--logo__disabled": !props.paragraph.isSetup }
]);

const handleClick = (paragraph: CardParagraphType) => {
  emit("click-item", paragraph);
};
</script>

<template>
  <div :class="cardClass">
    <div
      class="list-card-item_detail bg-bg_color"
      @click="handleClick(paragraph)"
    >
      <el-row justify="space-between">
        <div :class="cardLogoClass">
          <shopIcon />
        </div>
        <div class="list-card-item_detail--operation">
          <el-tag
            :color="paragraph.isSetup ? '#00a870' : '#eee'"
            effect="dark"
            class="mx-1 list-card-item_detail--operation--tag"
          >
            {{ paragraph.isSetup ? "已启用" : "已停用" }}
          </el-tag>
          <el-dropdown trigger="click" :disabled="!paragraph.isSetup">
            <IconifyIconOffline :icon="More2Fill" class="text-[24px]" />
            <template #dropdown>
              <el-dropdown-menu :disabled="!paragraph.isSetup">
                <el-dropdown-item @click="handleClickManage(paragraph)">
                  管理
                </el-dropdown-item>
                <el-dropdown-item @click="handleClickDelete(paragraph)">
                  删除
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-row>
      <p class="list-card-item_detail--desc text-text_color_regular">
        {{ paragraph.content }}
      </p>
      <div class="mt-1">
        <el-text type="info" size="small"
          >{{ paragraph.characterNumber }}个字符</el-text
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
    padding: 24px 32px;

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
      font-size: 13px;
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
