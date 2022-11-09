<template>
  <div>
    <br />
    <b-container>
      <div>
        <h1>Register Prescription</h1>
        <form @submit.prevent="create" :disabled="!isFormValid">
          <b-form-group
            label="Patient"
            label-for="name"
          >
            <b-input :value="username" disabled />
          </b-form-group>

         <b-form-group
            label="End Date"
            label-for="endDate"
          >
          <b-form-datepicker id="example-datepicker"
            v-model="value"
            :options="endDate"
            value-as-date
            description="The data type is required"
            class="mb-2" ></b-form-datepicker>
            </b-form-group>

          <b-form-group
            description="The Title is required"
            label="Title"
            label-for="title"
            :invalid-feedback="invalidTitleFeedback"
            :state="isTitleValid"
          >
            <b-input
              v-model="title"
              type="text"
              :state="isTitleValid"
              placeholder="Enter a title"
            />
          </b-form-group>


          <b-form-group
            description="The description is required"
            label="Description Assessment"
            label-for="description"
            :invalid-feedback="invalidDescritpionFeedback"
            :state="isDescriptionValid"
          >
            <b-input
              v-model="description"
              type="text"
              placeholder="Enter a description"
            />
          </b-form-group>

          <br />

          <p class="text-danger" v-show="errorMsg">{{ errorMsg }}</p>
          <b-button @click.prevent="create" :disabled="!isFormValid"
            >Create</b-button
          >
          <nuxt-link to="/prescriptions" class="btn float-right">
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
      value: null,
    };
  },
  computed: {
    username() {
      return this.$route.params.username
    },
    obsId() {
      return this.$route.params.id
    },

    invalidTitleFeedback() {
      if (!this.title) {
        return null
      }
      return '';
    },
    isTitleValid() {
      if (!this.title) {
        return null
      }
      let titleLen = this.title.length
      if (titleLen < 3 || titleLen > 25) {
        return false
      }
      return true
    },
    invalidDescriptionFeedback() {
      if (!this.description) {
        return null
      }
      return '';
    },
    isDescriptionValid() {
      if (!this.description) {
        return null
      }
      let descriptionLen = this.description.length
      if (descriptionLen < 3 || descriptionLen > 25) {
        return false
      }
      return true
    },

    isFormValid() {
      return true
    },
  },
  methods: {
    dateFormat(day, month, year){
      let now = new Date(Date.now());
      return {
        hour: now.getHours(),
        minute: now.getMinutes(),
        second: now.getSeconds(),
        day: day,
        month: month,
        year: year,
      }
    },
    now() {
      let now = new Date(Date.now());
      return {
        hour: now.getHours(),
        minute: now.getMinutes(),
        second: now.getSeconds(),
        day: now.getDate(),
        month: now.getMonth() + 1,
        year: now.getFullYear(),
      }
    },
    reset() {
      this.errorMsg = false;
    },
    create() {
      this.$axios
        .$post(`/api/prescriptions/`, {
            patientUsername: this.username,
            startDate: this.now(),
            endDate: this.dateFormat(this.value.getDate(), this.value.getMonth() + 1, this.value.getFullYear()),
            title: this.title,
            description: this.description,
            observationId: parseInt(this.obsId),
        })
        .then((response) => {
          this.$router.push(`/observations/${this.username}`);
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
