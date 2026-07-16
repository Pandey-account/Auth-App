export default interface RegisterData {
  name: string;
  email: string;
  mobileNo: string;
  password: string;
  confirmPassword: string;
  imageFile: File | null;
}