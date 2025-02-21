<template>
  <div class="body">
    <div class="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="bg-white shadow rounded-lg">
        <!-- Profile Header -->
        <div class="px-4 py-5 sm:px-6 border-b border-gray-200">
          <h3 class="text-lg leading-6 font-medium text-gray-900">Profile</h3>
          <p class="mt-1 text-sm text-gray-500">Manage your account information</p>
        </div>

        <!-- Profile Content -->
        <div class="px-4 py-5 sm:p-6">
          <div class="flex items-start space-x-6">
            <!-- Profile Picture -->
            <div class="flex-shrink-0">
              <div class="h-24 w-24 rounded-full bg-gray-200 flex items-center justify-center">
                <span class="material-symbols-outlined text-4xl text-gray-400">account_circle</span>
              </div>
              <button class="mt-2 text-sm text-blue-600 hover:text-blue-500">Change photo</button>
            </div>

            <!-- Profile Information -->
            <div class="flex-1">
              <div class="grid grid-cols-1 gap-y-6 sm:grid-cols-2 sm:gap-x-8">
                <div>
                  <label class="block text-sm font-medium text-gray-700">First Name</label>
                  <div class="mt-1">
                    <input type="text"  class="shadow-sm focus:ring-blue-500 focus:border-blue-500 block w-full sm:text-sm border-gray-300 rounded-md" />
                  </div>
                </div>

                <div>
                  <label class="block text-sm font-medium text-gray-700">Last Name</label>
                  <div class="mt-1">
                    <input type="text"  class="shadow-sm focus:ring-blue-500 focus:border-blue-500 block w-full sm:text-sm border-gray-300 rounded-md" />
                  </div>
                </div>

                <div class="sm:col-span-2">
                  <label class="block text-sm font-medium text-gray-700">Email</label>
                  <div class="mt-1">
                    <input type="email" class="shadow-sm focus:ring-blue-500 focus:border-blue-500 block w-full sm:text-sm border-gray-300 rounded-md" />
                  </div>
                </div>

                <div class="sm:col-span-2">
                  <label class="block text-sm font-medium text-gray-700">Phone</label>
                  <div class="mt-1">
                    <input type="tel"  class="shadow-sm focus:ring-blue-500 focus:border-blue-500 block w-full sm:text-sm border-gray-300 rounded-md" />
                  </div>
                </div>
              </div>

              <!-- Save Button -->
              <div class="mt-6">
                <button class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                  Save Changes
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>

</script>

<style></style>
