import { Component } from '@angular/core';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [],
  template: `
    <header>
      <nav
        class="fixed z-30 w-full border-b border-gray-200 bg-white px-4 py-3"
      >
        <div class="mx-auto flex max-w-screen-2xl items-center justify-between">
          <div class="flex items-center justify-start">
            <a href="#" class="mr-12 flex">
              <img src="assets/logo.png" class="h-10" />
            </a>
            <div
              class="hidden w-full items-center justify-between lg:order-1 lg:flex lg:w-auto"
            >
              <ul
                class="mt-4 flex flex-col space-x-6 text-sm font-medium lg:mt-0 lg:flex-row xl:space-x-8"
              >
                <li>
                  <button
                    id="dropdownNavbarLink1"
                    data-dropdown-toggle="dropdownNavbar1"
                    class="flex w-full items-center justify-between border-b border-gray-100 py-2 pl-3 pr-4 font-medium text-gray-700 hover:bg-gray-50 md:w-auto md:border-0 md:p-0 md:hover:bg-transparent md:hover:text-primary-700 md:dark:hover:bg-transparent"
                  >
                    Préparation Assemblée
                    <svg
                      class="ml-1 h-4 w-4"
                      aria-hidden="true"
                      fill="currentColor"
                      viewBox="0 0 20 20"
                      xmlns="http://www.w3.org/2000/svg"
                    >
                      <path
                        fill-rule="evenodd"
                        d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                        clip-rule="evenodd"
                      ></path>
                    </svg>
                  </button>
                  <div
                    id="dropdownNavbar1"
                    class="z-20 hidden w-auto divide-y divide-gray-100 rounded bg-white font-normal shadow"
                  >
                    <ul
                      class="py-1 text-sm text-gray-700"
                      aria-labelledby="dropdownLargeButton"
                    >
                      <li>
                        <a href="#" class="block px-4 py-2 hover:bg-gray-100"
                          >Nouvelle Assemblée générale
                        </a>
                      </li>
                      <li>
                        <a href="#" class="block px-4 py-2 hover:bg-gray-100"
                          >Convocation des adhérents</a
                        >
                      </li>
                    </ul>
                  </div>
                </li>
                <li>
                  <button
                    id="dropdownNavbarLink2"
                    data-dropdown-toggle="dropdownNavbar2"
                    class="flex w-full items-center justify-between border-b border-gray-100 py-2 pl-3 pr-4 font-medium text-gray-700 hover:bg-gray-50 md:w-auto md:border-0 md:p-0 md:hover:bg-transparent md:hover:text-primary-700 md:dark:hover:bg-transparent"
                  >
                    Suivi Quorum
                    <svg
                      class="ml-1 h-4 w-4"
                      aria-hidden="true"
                      fill="currentColor"
                      viewBox="0 0 20 20"
                      xmlns="http://www.w3.org/2000/svg"
                    >
                      <path
                        fill-rule="evenodd"
                        d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                        clip-rule="evenodd"
                      ></path>
                    </svg>
                  </button>
                  <div
                    id="dropdownNavbar2"
                    class="z-20 hidden w-auto divide-y divide-gray-100 rounded bg-white font-normal shadow"
                  >
                    <ul
                      class="py-1 text-sm text-gray-700"
                      aria-labelledby="dropdownLargeButton"
                    >
                      <li>
                        <a href="#" class="block px-4 py-2 hover:bg-gray-100"
                          >Gestion des relances téléphoniques
                        </a>
                      </li>
                      <li>
                        <a href="#" class="block px-4 py-2 hover:bg-gray-100"
                          >Numérisation/Injection des documents</a
                        >
                      </li>
                      <li>
                        <a href="#" class="block px-4 py-2 hover:bg-gray-100"
                          >Qualification des documents</a
                        >
                      </li>
                      <li>
                        <a href="#" class="block px-4 py-2 hover:bg-gray-100"
                          >Gestion des compléments/rejets</a
                        >
                      </li>
                      <li>
                        <a href="#" class="block px-4 py-2 hover:bg-gray-100"
                          >Enregistrement adhérent</a
                        >
                      </li>
                    </ul>
                  </div>
                </li>
                <li>
                  <button
                    id="dropdownNavbarLink3"
                    data-dropdown-toggle="dropdownNavbar3"
                    class="flex w-full items-center justify-between border-b border-gray-100 py-2 pl-3 pr-4 font-medium text-gray-700 hover:bg-gray-50 md:w-auto md:border-0 md:p-0 md:hover:bg-transparent md:hover:text-primary-700"
                  >
                    Consultation générale
                    <svg
                      class="ml-1 h-4 w-4"
                      aria-hidden="true"
                      fill="currentColor"
                      viewBox="0 0 20 20"
                      xmlns="http://www.w3.org/2000/svg"
                    >
                      <path
                        fill-rule="evenodd"
                        d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                        clip-rule="evenodd"
                      ></path>
                    </svg>
                  </button>
                  <div
                    id="dropdownNavbar3"
                    class="z-20 hidden w-auto divide-y divide-gray-100 rounded bg-white font-normal shadow"
                  >
                    <ul
                      class="py-1 text-sm text-gray-700"
                      aria-labelledby="dropdownLargeButton"
                    >
                      <li>
                        <a href="#" class="block px-4 py-2 hover:bg-gray-100"
                          >Assemblée en cours
                        </a>
                      </li>
                      <li>
                        <a href="#" class="block px-4 py-2 hover:bg-gray-100"
                          >Assemblées antérieures
                        </a>
                      </li>
                    </ul>
                  </div>
                </li>
                <li>
                  <button
                    id="dropdownNavbarLink4"
                    data-dropdown-toggle="dropdownNavbar4"
                    class="flex w-full items-center justify-between border-b border-gray-100 py-2 pl-3 pr-4 font-medium text-gray-700 hover:bg-gray-50 md:w-auto md:border-0 md:p-0 md:hover:bg-transparent md:hover:text-primary-700"
                  >
                    Administration
                    <svg
                      class="ml-1 h-4 w-4"
                      aria-hidden="true"
                      fill="currentColor"
                      viewBox="0 0 20 20"
                      xmlns="http://www.w3.org/2000/svg"
                    >
                      <path
                        fill-rule="evenodd"
                        d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                        clip-rule="evenodd"
                      ></path>
                    </svg>
                  </button>
                  <div
                    id="dropdownNavbar4"
                    class="z-20 hidden w-auto divide-y divide-gray-100 rounded bg-white font-normal shadow"
                  >
                    <ul
                      class="py-1 text-sm text-gray-700"
                      aria-labelledby="dropdownLargeButton"
                    >
                      <li>
                        <a href="#" class="block px-4 py-2 hover:bg-gray-100"
                          >Gestion utilisateurs
                        </a>
                      </li>
                      <li>
                        <a
                          href="#"
                          class="block px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white"
                          >Habilitations</a
                        >
                      </li>
                      <li>
                        <a href="#" class="block px-4 py-2 hover:bg-gray-100"
                          >Paramétrage</a
                        >
                      </li>
                    </ul>
                  </div>
                </li>
              </ul>
            </div>
          </div>
          <div class="flex items-center justify-between lg:order-2">
            <!-- Apps -->
            <button
              type="button"
              data-dropdown-toggle="userMenuDropdown"
              class="rounded-lg p-2 text-gray-500 hover:bg-gray-100 hover:text-gray-900 focus:ring-4 focus:ring-gray-300"
            >
              <span class="sr-only">View notifications</span>
              <!-- Icon -->
              <svg
                class="h-6 w-6"
                fill="currentColor"
                viewBox="0 0 20 20"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  d="M5 3a2 2 0 00-2 2v2a2 2 0 002 2h2a2 2 0 002-2V5a2 2 0 00-2-2H5zM5 11a2 2 0 00-2 2v2a2 2 0 002 2h2a2 2 0 002-2v-2a2 2 0 00-2-2H5zM11 5a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2V5zM11 13a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2v-2z"
                ></path>
              </svg>
            </button>
            <!-- Dropdown menu -->
            <div
              class="z-50 my-4 hidden w-56 list-none divide-y divide-gray-100 rounded bg-white text-base shadow"
              id="userMenuDropdown"
            >
              <div class="px-4 py-3">
                <span class="block text-sm font-semibold text-gray-900"
                  >Imad Lamrani</span
                >
                <span class="block truncate text-sm font-light text-gray-500"
                  >lamrani&#64;cimr.ma</span
                >
              </div>

              <ul
                class="py-1 font-light text-gray-500"
                aria-labelledby="dropdown"
              >
                <li>
                  <a href="#" class="block px-4 py-2 text-sm hover:bg-gray-100"
                    >Déconnexion</a
                  >
                </li>
              </ul>
            </div>

            <button
              type="button"
              id="toggleMobileMenuButton"
              data-collapse-toggle="toggleMobileMenu"
              class="items-center rounded-lg p-2 text-gray-500 hover:bg-gray-100 hover:text-gray-900 focus:ring-4 focus:ring-gray-300 md:ml-2 lg:hidden"
            >
              <span class="sr-only">Open menu</span>
              <svg
                class="h-6 w-6"
                aria-hidden="true"
                fill="currentColor"
                viewBox="0 0 20 20"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  fill-rule="evenodd"
                  d="M3 5a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 10a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 15a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1z"
                  clip-rule="evenodd"
                ></path>
              </svg>
            </button>
          </div>
        </div>
      </nav>
      <nav class="bg-white dark:bg-gray-900">
        <!-- Mobile menu -->
        <ul
          id="toggleMobileMenu"
          class="mt-0 hidden w-full flex-col pt-16 text-sm font-medium lg:hidden"
        >
          <li class="block border-b dark:border-gray-700">
            <a
              href="#"
              class="block px-4 py-3 text-gray-900 dark:text-white lg:px-0 lg:py-0 lg:hover:underline"
              aria-current="page"
              >Home</a
            >
          </li>
          <li class="block border-b dark:border-gray-700">
            <a
              href="#"
              class="block px-4 py-3 text-gray-900 dark:text-white lg:px-0 lg:py-0 lg:hover:underline"
              >Messages</a
            >
          </li>
          <li class="block border-b dark:border-gray-700">
            <a
              href="#"
              class="block px-4 py-3 text-gray-900 dark:text-white lg:px-0 lg:py-0 lg:hover:underline"
              >Profile</a
            >
          </li>
          <li class="block border-b dark:border-gray-700">
            <a
              href="#"
              class="block px-4 py-3 text-gray-900 dark:text-white lg:px-0 lg:py-0 lg:hover:underline"
              >Settings</a
            >
          </li>
          <li class="block border-b dark:border-gray-700">
            <button
              type="button"
              data-collapse-toggle="dropdownMobileNavbar"
              class="flex w-full items-center justify-between px-4 py-3 text-gray-900 dark:text-white lg:px-0 lg:py-0 lg:hover:underline"
            >
              Dropdown
              <svg
                class="h-6 w-6 text-gray-500 dark:text-gray-400"
                aria-hidden="true"
                fill="currentColor"
                viewBox="0 0 20 20"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  fill-rule="evenodd"
                  d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z"
                  clip-rule="evenodd"
                ></path>
              </svg>
            </button>
            <ul id="dropdownMobileNavbar" class="hidden">
              <li class="block border-b border-t dark:border-gray-700">
                <a
                  href="#"
                  class="block px-4 py-3 text-gray-900 dark:text-white lg:px-0 lg:py-0 lg:hover:underline"
                  >Item 1</a
                >
              </li>
              <li class="block border-b dark:border-gray-700">
                <a
                  href="#"
                  class="block px-4 py-3 text-gray-900 dark:text-white lg:px-0 lg:py-0 lg:hover:underline"
                  >Item 2</a
                >
              </li>
              <li class="block">
                <a
                  href="#"
                  class="block px-4 py-3 text-gray-900 dark:text-white lg:px-0 lg:py-0 lg:hover:underline"
                  >Item 3</a
                >
              </li>
            </ul>
          </li>
        </ul>
      </nav>
    </header>
  `,
  styles: ``,
})
export class NavbarComponent {}
