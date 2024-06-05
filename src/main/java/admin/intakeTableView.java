package admin;

public class intakeTableView {
    public static class Intake {
        private String intakeCode;

        public Intake(String intakeCode) {
            this.intakeCode = intakeCode;
        }

        public String getIntakeCode() {
            return intakeCode;
        }

        public void setIntakeCode(String intakeCode) {
            this.intakeCode = intakeCode;
        }
    }
}
