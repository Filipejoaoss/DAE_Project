<template>
  <div>
    <br />
    <b-container>
      <div>
        <h1>Register Observation</h1>
        <form @submit.prevent="create" :disabled="!isFormValid">
          <b-form-group
            label="Patient"
            label-for="name"
          >
            <b-input :value="username" disabled />
          </b-form-group>

          <label>Biomedic Data Type</label>
          <b-form-select
            description="The data type is required"
            :options="dataTypes"
            v-model="selectedDataType"
            :state="isSelectValid"
            :invalid-feedback="invalidSelectFeedback"
          >
            <template #first>
              <b-form-select-option :value="null" disabled
                >-- Please select a Biomedic Data Type --</b-form-select-option
              >
            </template>
          </b-form-select>

          <br />

          <b-form-group
            description="The value is required"
            label="Value"
            label-for="value"
            :invalid-feedback="invalidValueFeedback"
            :state="isValueValid"
          >
            <b-input
              v-model="value"
              type="number"
              :state="isValueValid"
              placeholder="Enter a value"
            />
          </b-form-group>


          <b-form-group
            description="The unity is required"
            label="Qualitative Assessment"
            label-for="qualitative"
          >
            <b-input
              v-model="qualitative"
              type="text"
              placeholder="Enter the qualitative assessment"
            />
          </b-form-group>

          <br />

          <p class="text-danger" v-show="errorMsg">{{ errorMsg }}</p>
          <b-button @click.prevent="create" :disabled="!isFormValid"
            >Create</b-button
          >
          <nuxt-link to="/biomedicDataTypes" class="btn float-right">
            <b-button>Back</b-button>
          </nuxt-link>
        </form>
      </div>
    </b-container>
  </div>
</template>
<script>
export default {
  middleware: "observations",
  layout: "navbar",
  data() {
    return {
      errorMsg: "",
      dataTypes: [],
      selectedDataType: null,
      value: 0,
      qualitative: "",
    };
  },
  computed: {
    username() {
      return this.$route.params.username;
    },
    invalidSelectFeedback() {
      if (this.selectedDataType == null) {
        return "Please Select a Data Type";
      }
      return "";
    },
    isSelectValid() {
      return this.selectedDataType != null
    },

    invalidValueFeedback() {
      if (isNaN(this.value)) {
        return "Please Select a Data Type";
      }
      return "";
    },
    isValueValid() {
      return this.selectedDataType != null
    },

    isFormValid() {
      return this.isSelectValid && this.isValueValid;
    },
  },
  methods: {
    now() {
      let now = new Date(Date.now());
      return {
        hour: now.getHours(),
        minute: now.getMinutes(),
        second: now.getSeconds(),
        day: now.getDate(),
        month: now.getMonth() + 1,
        year: now.getFullYear(),
      };
    },
    reset() {
      this.errorMsg = false;
    },
    create() {
      console.log(this.selectedDataType);
      this.$axios
        .$post(`/api/observations/${this.username}`, {
          patientUsername: this.username,
          examiner: `${this.$auth.user.sub}@${navigator.appCodeName}(${navigator.oscpu})`,
          dateTime: this.now(),
          biomedicDataType: { id: this.selectedDataType },
          qualitative: this.qualitative,
          value: this.value,
        })
        .then((response) => {
          this.$router.push(`/observations/${username}`);
          this.$toast.sucess("Created!").goAway(3000);
        })
        .catch((error) => {
          this.$toast.error(error.data).goAway(3000);
        });
    },
  },
  created() {
    this.$axios.get(`/api/biomedicDataTypes/`).then((response) => {
      const data = response.data;
      this.dataTypes = data.map((item) => {
        return {
          text: item.name,
          value: item.id,
        };
      });
    });
  },
};
</script>
