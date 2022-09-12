// AccountInfo.vue

<template>
  <div>
    <select id="account-info" v-model="selected" @change="onChange($event)">
      <option v-for="field in fields" :key="field.key">
        {{ field.key }}
      </option>
    </select>
    <select v-model="sub_drop">
      <option v-for="field in subDrop" :key="field.key">
        {{ field.key }}
      </option>
    </select>
  </div>

</template>

<script>
export default {
  props: ['fields', 'sub_drop_value'],
  methods: {
    onChange(event) {
      this.subDrop = this.sub_drop_values[event.target.value];
    }
  }
}

</script>