package com.example.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DataWarehouse {
    private static ArrayList<Category> categories;
    private static ArrayList<Product> products;
    private static ArrayList<Employee> employees;
    private static ArrayList<Customer> customers;
    private static ArrayList<Order> orders;
    private static ArrayList<OrderDetail> orderDetails;

    public static ArrayList<Category> getCategories() {
        if (categories == null) {
            categories = new ArrayList<>();
            categories.add(new Category("C1", "Mỳ tôm", "Các loại mỳ tôm chống đói"));
            categories.add(new Category("C2", "Rau củ quả", "Rau củ quả tươi"));
            categories.add(new Category("C3", "Nước uống có gas", "Nước uống có gas"));
            categories.add(new Category("C4", "Trái cây", "Trái cây Vietgap"));
            categories.add(new Category("C5", "Thịt", "Thịt các loại"));
        }
        return categories;
    }

    public static ArrayList<Product> getProducts() {
        if (products == null) {
            products = new ArrayList<>();
            ArrayList<Category> cats = getCategories();
            products.add(new Product("P1", "Mỳ Hảo Hảo", 100, 5.0, 0, 0.1, cats.get(0).getCategoryID()));
            products.add(new Product("P2", "Mỳ Omachi", 50, 8.0, 0, 0.1, cats.get(0).getCategoryID()));
            products.add(new Product("P3", "Mỳ Kokomi", 200, 4.5, 0.05, 0.1, cats.get(0).getCategoryID()));
            products.add(new Product("P4", "Mỳ 3 Miền", 150, 4.0, 0, 0.1, cats.get(0).getCategoryID()));
            products.add(new Product("P5", "Mỳ Koreno", 30, 15.0, 0, 0.1, cats.get(0).getCategoryID()));
            products.add(new Product("P6", "Mỳ Cung Đình", 40, 12.0, 0, 0.1, cats.get(0).getCategoryID()));
            products.add(new Product("P7", "Bắp cải", 20, 15.0, 0, 0.05, cats.get(1).getCategoryID()));
            products.add(new Product("P8", "Cà rốt", 40, 20.0, 0, 0.05, cats.get(1).getCategoryID()));
            products.add(new Product("P9", "Khoai tây", 60, 18.0, 0, 0.05, cats.get(1).getCategoryID()));
            products.add(new Product("P10", "Hành tây", 30, 22.0, 0, 0.05, cats.get(1).getCategoryID()));
            products.add(new Product("P11", "Cà chua", 50, 25.0, 0.1, 0.05, cats.get(1).getCategoryID()));
            products.add(new Product("P12", "Coca Cola", 100, 10.0, 0.1, 0.1, cats.get(2).getCategoryID()));
            products.add(new Product("P13", "Pepsi", 100, 10.0, 0.1, 0.1, cats.get(2).getCategoryID()));
            products.add(new Product("P14", "7Up", 80, 10.0, 0, 0.1, cats.get(2).getCategoryID()));
            products.add(new Product("P15", "Sprite", 80, 10.0, 0, 0.1, cats.get(2).getCategoryID()));
            products.add(new Product("P16", "Sting dâu", 120, 12.0, 0, 0.1, cats.get(2).getCategoryID()));
            products.add(new Product("P17", "Mirinda Cam", 90, 11.0, 0, 0.1, cats.get(2).getCategoryID()));
            products.add(new Product("P18", "Fanta Xá xị", 70, 11.0, 0.05, 0.1, cats.get(2).getCategoryID()));
            products.add(new Product("P19", "Táo Mỹ", 40, 80.0, 0, 0.05, cats.get(3).getCategoryID()));
            products.add(new Product("P20", "Cam Sành", 100, 30.0, 0.1, 0.05, cats.get(3).getCategoryID()));
            products.add(new Product("P21", "Nho Ninh Thuận", 30, 60.0, 0, 0.05, cats.get(3).getCategoryID()));
            products.add(new Product("P22", "Chuối sứ", 50, 20.0, 0, 0.05, cats.get(3).getCategoryID()));
            products.add(new Product("P23", "Xoài Cát", 25, 90.0, 0, 0.05, cats.get(3).getCategoryID()));
            products.add(new Product("P24", "Thịt heo ba chỉ", 15, 140.0, 0, 0.1, cats.get(4).getCategoryID()));
            products.add(new Product("P25", "Thịt bò tái", 10, 280.0, 0.05, 0.1, cats.get(4).getCategoryID()));
            products.add(new Product("P26", "Cánh gà", 25, 90.0, 0, 0.1, cats.get(4).getCategoryID()));
            products.add(new Product("P27", "Sườn non", 12, 180.0, 0, 0.1, cats.get(4).getCategoryID()));
            products.add(new Product("P28", "Trứng gà ta", 100, 4.0, 0, 0.1, cats.get(4).getCategoryID()));
            products.add(new Product("P29", "Thịt đùi heo", 20, 130.0, 0.1, 0.1, cats.get(4).getCategoryID()));
        }
        return products;
    }

    public static ArrayList<Employee> getEmployee() {
        if (employees == null) {
            employees = new ArrayList<>();
            employees.add(new Employee("E1", "Phạm Huyền", "0976106992", "Đà Nẵng"));
            employees.add(new Employee("E2", "Nguyễn Văn An", "0901234567", "Hà Nội"));
            employees.add(new Employee("E3", "Trần Thị Bình", "0912345678", "Hải Phòng"));
            employees.add(new Employee("E4", "Lê Hoàng Cường", "0923456789", "Quảng Ninh"));
            employees.add(new Employee("E5", "Phạm Minh Đức", "0934567890", "Nam Định"));
            employees.add(new Employee("E6", "Đỗ Thu Hà", "0945678901", "Thanh Hóa"));
            employees.add(new Employee("E7", "Ngô Quang Huy", "0956789012", "Nghệ An"));
            employees.add(new Employee("E8", "Hoàng Kim Liên", "0967890123", "Thừa Thiên Huế"));
            employees.add(new Employee("E9", "Bùi Tiến Mạnh", "0978901234", "Đà Nẵng"));
            employees.add(new Employee("E10", "Vũ Tuyết Nhung", "0989012345", "Quảng Nam"));
        }
        return employees;
    }

    public static ArrayList<Customer> getCustomers() {
        if (customers == null) {
            customers = new ArrayList<>();
            Calendar cal = Calendar.getInstance();
            String[] firstNames = {"Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Huỳnh", "Phan", "Vũ", "Võ", "Đặng"};
            String[] lastNames = {"An", "Bình", "Cường", "Đức", "Hà", "Huy", "Liên", "Mạnh", "Nhung", "Phương"};
            for (int i = 1; i <= 50; i++) {
                String id = String.format(Locale.getDefault(), "Cus%02d", i);
                String name = firstNames[i % 10] + " " + lastNames[(i / 5) % 10];
                customers.add(new Customer(id, name, "09123456" + i, "cus" + i + "@gmail.com", new Date(), "Hà Nội"));
            }
        }
        return customers;
    }

    public static ArrayList<Order> getOrders() {
        if (orders == null) {
            orders = new ArrayList<>();
            ArrayList<Customer> custs = getCustomers();
            ArrayList<Employee> emps = getEmployee();
            Calendar cal = Calendar.getInstance();
            cal.set(2024, Calendar.JANUARY, 1, 8, 0, 0);

            OrderStatus[] statuses = {OrderStatus.COMPLETED, OrderStatus.NOT_PAYMENT, OrderStatus.ON_LOGISTICS, OrderStatus.COMPLAINT};

            for (int i = 1; i <= 200; i++) { // Giảm xuống 200 để bớt nặng
                String orderID = String.format(Locale.getDefault(), "ORD%04d", i);
                cal.add(Calendar.HOUR, 12);
                Order order = new Order(orderID, custs.get(i % custs.size()).getCusID(), emps.get(i % emps.size()).getId(), cal.getTime());
                order.setOrderStatus(statuses[i % statuses.length]);
                orders.add(order);
            }
        }
        return orders;
    }

    public static ArrayList<OrderDetail> getOrderDetails() {
        if (orderDetails == null) {
            orderDetails = new ArrayList<>();
            ArrayList<Order> ods = getOrders();
            ArrayList<Product> prods = getProducts();
            int detailCounter = 1;
            for (int i = 0; i < ods.size(); i++) {
                Order order = ods.get(i);
                int numItems = 1 + (i % 3);
                for (int j = 0; j < numItems; j++) {
                    Product product = prods.get((i + j) % prods.size());
                    orderDetails.add(new OrderDetail(String.format(Locale.getDefault(), "Odt%05d", detailCounter++), 
                        order.getOrderID(), product.getProductId(), 1 + j, product.getPrice(), product.getCoupon(), product.getVAT()));
                }
            }
        }
        return orderDetails;
    }

    public static double sumOfMoneyForOrder(Order order) {
        double sum = 0;
        ArrayList<OrderDetail> details = getOrderDetails();
        String id = order.getOrderID();
        for (OrderDetail detail : details) {
            if (detail.getOrderID().equals(id)) {
                sum += (detail.getQuantity() * detail.getPrice() - detail.getCoupon()) * (1 + detail.getVAT());
            }
        }
        return sum;
    }

    public static ArrayList<Order> filterOrders(OrderStatus status, Date fromDate, Date toDate) {
        ArrayList<Order> allOrders = getOrders();
        ArrayList<Order> results = new ArrayList<>();

        Calendar calFrom = Calendar.getInstance();
        calFrom.setTime(fromDate);
        resetTime(calFrom);

        Calendar calTo = Calendar.getInstance();
        calTo.setTime(toDate);
        resetTime(calTo);

        for (Order order : allOrders) {
            // Check status
            if (status != OrderStatus.ALL && order.getOrderStatus() != status) continue;

            // Check date
            Calendar calOrder = Calendar.getInstance();
            calOrder.setTime(order.getOrderDate());
            resetTime(calOrder);

            if (!calOrder.before(calFrom) && !calOrder.after(calTo)) {
                results.add(order);
            }
        }
        return results;
    }

    private static void resetTime(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    // Các hàm cũ giữ lại để tương thích nhưng gọi hàm chung
    public static ArrayList<Order> filterOrdersByStatus(OrderStatus status) {
        return filterOrders(status, new Date(0), new Date(Long.MAX_VALUE));
    }

    public static ArrayList<Order> filterOrdersByDate(Date fromDate, Date toDate) {
        return filterOrders(OrderStatus.ALL, fromDate, toDate);
    }
}
