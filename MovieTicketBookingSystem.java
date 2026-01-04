import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Random;

public class MovieTicketBookingSystem {

    JFrame frame;
    HashMap<String, String> users = new HashMap<>();

    String currentUser, selectedCinema, selectedMovie, selectedTime, paymentMode;
    String selectedSeats = "";
    int seatCount = 0;

    JCheckBox[] seats = new JCheckBox[25];

    Font titleFont = new Font("Segoe UI", Font.BOLD, 28);
    Font normalFont = new Font("Segoe UI", Font.PLAIN, 16);
    Font buttonFont = new Font("Segoe UI", Font.BOLD, 18);
    Font boldFont = new Font("Segoe UI", Font.BOLD, 16);

    Color RED = new Color(220, 20, 60);
    Color GREEN = new Color(50, 205, 50);
    Color GRAY = new Color(169, 169, 169);
    Color CARD_BG = new Color(250, 250, 250);

    String[] cinemas = {"PVR Cinemas", "INOX", "Cinepolis"};
    String[] cinemaImgs = {"cinema1.jpg", "cinema2.jpg", "cinema3.jpg"};

    String[] movies = {"Dhurandhar", "Avengers", "KGF"};
    String[] movieImgs = {"dhurandhar.jpg", "avengers.jpg", "kgf.jpg"};

    public MovieTicketBookingSystem() {
        frame = new JFrame("Movie Ticket Booking System");
        frame.setSize(1100, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        showRegisterPage();
        frame.setVisible(true);
    }

   
    JPanel whiteCard(int w, int h) {
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(w, h));
        p.setBackground(CARD_BG);
        p.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        return p;
    }

    void setPage(JPanel card) {
        JPanel bg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(200, 220, 255),
                        0, getHeight(), new Color(245, 245, 245));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setLayout(new GridBagLayout());
        bg.add(card);
        frame.setContentPane(bg);
        frame.revalidate();
        frame.repaint();
    }

    JLabel title(String t) {
        JLabel l = new JLabel(t, SwingConstants.CENTER);
        l.setFont(titleFont);
        l.setForeground(RED);
        return l;
    }

    JButton btn(String t) {
        JButton b = new JButton(t);
        b.setFont(normalFont);
        b.setBackground(RED);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(180, 38));
        return b;
    }

    JLabel boldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(boldFont);
        return l;
    }

    // ---------- REGISTER ----------
    void showRegisterPage() {
        JPanel card = whiteCard(450, 420);
        card.setLayout(new GridLayout(8, 1, 10, 10));

        JTextField user = new JTextField();
        JPasswordField pass = new JPasswordField();

        user.setBorder(new TitledBorder("Username"));
        pass.setBorder(new TitledBorder("Password"));

        JButton reg = btn("Register");
        reg.addActionListener(e -> {
            if (!user.getText().isEmpty() && pass.getPassword().length > 0) {
                users.put(user.getText(), new String(pass.getPassword()));
                JOptionPane.showMessageDialog(frame, "Registration Successful!");
                showLoginPage();
            } else {
                JOptionPane.showMessageDialog(frame, "Enter Username and Password!");
            }
        });

        JButton loginBtn = btn("Already have an account? Login");
        loginBtn.addActionListener(e -> showLoginPage());

        card.add(title("User Registration"));
        card.add(user);
        card.add(pass);
        card.add(reg);
        card.add(loginBtn);

        setPage(card);
    }

    // ---------- LOGIN ----------
    void showLoginPage() {
        JPanel card = whiteCard(450, 380);
        card.setLayout(new GridLayout(7, 1, 10, 10));

        JTextField user = new JTextField();
        JPasswordField pass = new JPasswordField();

        user.setBorder(new TitledBorder("Username"));
        pass.setBorder(new TitledBorder("Password"));

        JButton login = btn("Login");
        login.addActionListener(e -> {
            if (users.containsKey(user.getText()) &&
                    users.get(user.getText()).equals(new String(pass.getPassword()))) {
                currentUser = user.getText();
                showCinemaPage();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Login");
            }
        });

      
        JButton forgotUser = btn("Forgot Username");
        JButton forgotPass = btn("Forgot Password");

        forgotUser.addActionListener(e ->
                JOptionPane.showMessageDialog(frame,
                        users.isEmpty() ? "No users registered!" : "Registered users: " + users.keySet()));

        forgotPass.addActionListener(e -> {
            String u = JOptionPane.showInputDialog(frame, "Enter your username:");
            if (u != null && users.containsKey(u)) {
                JOptionPane.showMessageDialog(frame, "Password: " + users.get(u));
            } else {
                JOptionPane.showMessageDialog(frame, "User not found!");
            }
        });

        card.add(title("User Login"));
        card.add(user);
        card.add(pass);
        card.add(login);
        card.add(forgotUser);
        card.add(forgotPass);

        setPage(card);
    }

    // ---------- CINEMA ----------
    void showCinemaPage() {
        showCardSelection("Select Cinema", cinemas, cinemaImgs, true);
    }

    // ---------- MOVIE ----------
    void showMoviePage() {
        showCardSelection("Select Movie", movies, movieImgs, false);
    }

    void showCardSelection(String heading, String[] names, String[] imgs, boolean isCinema) {
        JPanel card = whiteCard(950, 480);
        card.setLayout(new BorderLayout(15, 15));

        JPanel grid = new JPanel(new GridLayout(1, 3, 25, 25));
        grid.setBorder(new EmptyBorder(20, 20, 20, 20));
        grid.setOpaque(false);

        for (int i = 0; i < names.length; i++) {
            int idx = i;
            JPanel item = new JPanel(new BorderLayout());
            item.setBorder(new CompoundBorder(
                    new LineBorder(Color.LIGHT_GRAY, 1, true),
                    new DropShadowBorder()));
            item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            ImageIcon icon;
            try {
                icon = new ImageIcon(new ImageIcon(imgs[i])
                        .getImage().getScaledInstance(260, 360, Image.SCALE_SMOOTH));
            } catch (Exception e) {
                icon = new ImageIcon(new BufferedImage(260, 360, BufferedImage.TYPE_INT_RGB));
            }

            JLabel img = new JLabel(icon);
            item.add(img);

            item.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    item.setBorder(new CompoundBorder(
                            new LineBorder(RED, 2, true),
                            new DropShadowBorder()));
                }

                public void mouseExited(MouseEvent e) {
                    item.setBorder(new CompoundBorder(
                            new LineBorder(Color.LIGHT_GRAY, 1, true),
                            new DropShadowBorder()));
                }

                public void mouseClicked(MouseEvent e) {
                    if (isCinema) {
                        selectedCinema = names[idx];
                        showMoviePage();
                    } else {
                        selectedMovie = names[idx];
                        showTimePage();
                    }
                }
            });

            grid.add(item);
        }

        card.add(title(heading), BorderLayout.NORTH);
        card.add(grid, BorderLayout.CENTER);
        setPage(card);
    }

    // ---------- TIME ----------
    void showTimePage() {
        JPanel card = whiteCard(600, 400);
        card.setLayout(new BorderLayout(20, 20));

        JPanel timesPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        timesPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        timesPanel.setOpaque(false);

        String[] times = {"10:00 AM", "1:30 PM", "6:00 PM", "9:30 PM"};

        for (String time : times) {
            JButton timeBtn = new JButton(time);
            timeBtn.setFont(buttonFont);
            timeBtn.setBackground(RED);
            timeBtn.setForeground(Color.WHITE);
            timeBtn.setFocusPainted(false);

            timeBtn.addActionListener(e -> {
                selectedTime = time;
                showSeatPage();
            });

            timesPanel.add(timeBtn);
        }

        card.add(title("Select Time"), BorderLayout.NORTH);
        card.add(timesPanel, BorderLayout.CENTER);
        setPage(card);
    }

    // ---------- SEAT ----------
    void showSeatPage() {
        JPanel card = whiteCard(750, 550);
        card.setLayout(new BorderLayout(10, 10));

        JPanel grid = new JPanel(new GridLayout(5, 5, 12, 12));
        grid.setBorder(new EmptyBorder(20, 40, 20, 40));

        String rows = "ABCDE";
        int k = 0;
        Random rand = new Random();

        for (int r = 0; r < 5; r++) {
            for (int c = 1; c <= 5; c++) {
                JCheckBox seat = new JCheckBox(rows.charAt(r) + "" + c);
                seat.setFont(normalFont);
                seat.setOpaque(true);

                boolean booked = rand.nextInt(10) < 2;
                if (booked) {
                    seat.setBackground(GRAY);
                    seat.setEnabled(false);
                    seat.setForeground(Color.WHITE);
                } else {
                    seat.setBackground(GREEN);
                    seat.addItemListener(e -> {
                        seat.setBackground(seat.isSelected() ? RED : GREEN);
                        seat.setForeground(seat.isSelected() ? Color.WHITE : Color.BLACK);
                    });
                }

                seats[k++] = seat;
                grid.add(seat);
            }
        }

        JButton pay = btn("Proceed to Payment");
        pay.addActionListener(e -> {
            selectedSeats = "";
            seatCount = 0;
            for (JCheckBox s : seats)
                if (s.isSelected()) {
                    seatCount++;
                    selectedSeats += s.getText() + " ";
                }
            if (seatCount == 0)
                JOptionPane.showMessageDialog(frame, "Please select at least one seat!");
            else
                showPaymentPage();
        });

        card.add(title("Select Seats"), BorderLayout.NORTH);
        card.add(grid, BorderLayout.CENTER);
        card.add(pay, BorderLayout.SOUTH);
        setPage(card);
    }

    // ---------- PAYMENT ----------
    void showPaymentPage() {
        JPanel card = whiteCard(500, 350);
        card.setLayout(new GridBagLayout());

        JPanel inner = new JPanel(new GridLayout(6, 1, 10, 10));
        inner.setOpaque(false);

        JRadioButton cardPay = new JRadioButton("Card");
        JRadioButton upi = new JRadioButton("UPI");
        JRadioButton cash = new JRadioButton("Cash");

        ButtonGroup g = new ButtonGroup();
        g.add(cardPay);
        g.add(upi);
        g.add(cash);

        JLabel amount = boldLabel("Amount to Pay: ₹" + seatCount * 200);
        amount.setHorizontalAlignment(SwingConstants.CENTER);

        JButton pay = btn("Confirm Payment");
        pay.addActionListener(e -> {
            if (!cardPay.isSelected() && !upi.isSelected() && !cash.isSelected()) {
                JOptionPane.showMessageDialog(frame, "Select Payment Mode!");
            } else {
                paymentMode = cardPay.isSelected() ? "Card" : upi.isSelected() ? "UPI" : "Cash";
                showBillPage();
            }
        });

        inner.add(title("Payment"));
        inner.add(cardPay);
        inner.add(upi);
        inner.add(cash);
        inner.add(amount);
        inner.add(pay);

        card.add(inner);
        setPage(card);
    }

    // ---------- BILL ----------
    void showBillPage() {
        JPanel card = whiteCard(520, 520);
        card.setLayout(new GridBagLayout());

        JPanel inner = new JPanel();
        inner.setPreferredSize(new Dimension(420, 450));
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);

        JLabel heading = new JLabel("BOOKING DETAILS");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        heading.setForeground(RED);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        inner.add(Box.createVerticalStrut(20));
        inner.add(heading);
        inner.add(Box.createVerticalStrut(25));

        JPanel details = new JPanel(new GridLayout(0, 2, 15, 12));
        details.setOpaque(false);

        details.add(boldLabel("User"));          details.add(new JLabel(currentUser));
        details.add(boldLabel("Cinema"));        details.add(new JLabel(selectedCinema));
        details.add(boldLabel("Movie"));         details.add(new JLabel(selectedMovie));
        details.add(boldLabel("Show Time"));     details.add(new JLabel(selectedTime));
        details.add(boldLabel("Seats"));         details.add(new JLabel(selectedSeats));
        details.add(boldLabel("Payment Mode"));  details.add(new JLabel(paymentMode));

        inner.add(details);
        inner.add(Box.createVerticalStrut(20));

        JLabel amount = new JLabel("Total Amount : ₹" + seatCount * 200);
        amount.setFont(new Font("Segoe UI", Font.BOLD, 18));
        amount.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.add(amount);

        inner.add(Box.createVerticalStrut(25));

        JButton printReceipt = btn("Confirm & Print Ticket");
        printReceipt.setAlignmentX(Component.CENTER_ALIGNMENT);
        printReceipt.addActionListener(e ->
                JOptionPane.showMessageDialog(frame, "Ticket Confirmed!"));

        JButton logout = btn("Logout");
        logout.setAlignmentX(Component.CENTER_ALIGNMENT);
        logout.addActionListener(e -> showLoginPage());

        inner.add(printReceipt);
        inner.add(Box.createVerticalStrut(12));
        inner.add(logout);

        card.add(inner);
        setPage(card);
    }

    class DropShadowBorder extends AbstractBorder {
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            g.setColor(new Color(0, 0, 0, 40));
            g.fillRect(x + 4, y + 4, w - 4, h - 4);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MovieTicketBookingSystem::new);
    }
}