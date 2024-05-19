import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";

const API_URL = 'http://localhost:8080/api/v1/';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  #http = inject(HttpClient);

  getUsers() {
    return this.#http.get(API_URL + 'users');
  }
}
