<template>
  <div>
    <AppHeader/>
    <div class="filter-container">
      <div>
        <input id="option1" autocomplete="off" checked class="btn-check" name="options" type="radio">
        <label class="btn btn-secondary btn-sm" for="option1">AND</label>

        <input id="option2" autocomplete="off" class="btn-check" name="options" type="radio">
        <label class="btn btn-secondary btn-sm" for="option2">OR</label>

        <button class="btn-right btn btn-sm btn-primary" type="button">Add Group</button>
        <button class="btn-right btn btn-sm btn-primary" type="button">Add Rule</button>
      </div>

      <div class="opt-container container-fluid">
        <div class="m-md-2">
          <select v-model="selected" @change="onChange($event)">
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


        <div>
          <button class="btn-sm btn-primary btn position-relative" type="button">Delete</button>
        </div>
      </div>
      <button class="btn-right btn btn-sm btn-primary" type="button">Search</button>

    </div>

    <p><strong>Total No.of Records found in Experimental Database: {{ rows }}</strong></p>

    <div class="table-container justify-content-md-center overflow-auto table-bordered table-hover">
      <b-table
          id="my-table"
          :current-page="currentPage"
          :items="items"
          :per-page="perPage"
          small
      >
        <template #cell(mol_id)="data">
          <a href="#" @click="onChange">{{ data.value }}</a>
        </template>
      </b-table>
    </div>
    <b-pagination
        v-model="currentPage"
        :per-page="perPage"
        :total-rows="rows"
        align="right"
        aria-controls="my-table"
        class="pagination"
    />

    <div>
      <account-info :fields="fields" />
    </div>

  </div>

</template>

<script>
import AppHeader from "@/components/AppHeader";
import axios from "axios";
import DataSheet from "@/pages/DataSheet";
import AccountInfo from "@/pages/AccountInfo";

export default {
  name: "SearchPage",
  // eslint-disable-next-line vue/no-unused-components
  components: {AccountInfo, DataSheet, AppHeader},
  data() {
    return {
      dataMsg: "From Master",
      perPage: 7,
      currentPage: 1,
      displayCategory: false,
      selected: 'vghjv',
      sub_drop: "",
      subDrop: [],
      selectedFilter: {},
      sub_drop_values: {
        'rdb': [
          {key: "less"},
          {key: "greater"},
          {key: "equals"}
        ],

        'absorb': [
          {key: "less"}
        ]
      },

      fields: [
        {
          key: "rdb",
          sortable: true,
          href: "www.google.com"
        },
        {
          key: "mw_source",
          sortable: true
        },
        {
          key: "absorb",
          sortable: true
        }
      ],
      items: [],
    }
  },

  async created() {
    try {
      const res = await axios.get(`http://127.0.0.1:8000/api/molecule/`);
      this.items = res.data;
    } catch (error) {
      console.log(error);
    }
  },
  computed: {
    rows() {
      return this.items.length
    }
  },
  methods: {
    onChange(event) {
      this.subDrop = this.sub_drop_values[event.target.value];
    }
  }
}
</script>

<style scoped>
.filter-container {
  padding: 2% 2%;
  margin: 3% 20%;
  background-color: #f8f9fa;
}

.btn-right {
  float: right;
  margin-left: 2%;
}

/*.btn-del {*/
/*  left: 3px;*/
/*  display: flex;*/
/*  position: inherit;*/
/*  margin-right: 2px;*/
/*  margin-left: 7%;*/
/*}*/

.opt-container {
  height: 40px;
  display: inline-flex;
  border-style: solid;
  border-width: 1px;
  border-color: #007bff;
  background-color: white;
  border-radius: 10px;
  align-items: center;
}

.dropdown {
  align-items: center;
}

.table-container {
  padding-bottom: unset;
  margin-left: 4%;
  margin-right: 4%;
  margin-bottom: unset;
}

.pagination {
  margin-right: 4%;
}

.dropdown-item {
  color: black;
}
</style>