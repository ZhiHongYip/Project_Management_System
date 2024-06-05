package admin;

public class studentTableView {
    private String id;

    private String name;
    private String email;

    private String intake;

    public static class Student {
        private String id;
        private String name;
        private String email;
        private String intake;
        public Student(String id, String name, String email, String intake) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.intake = intake;

        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getIntake() {
            return intake;
        }

        public void setIntake(String intake) {
            this.intake = intake;
        }


    }
}
