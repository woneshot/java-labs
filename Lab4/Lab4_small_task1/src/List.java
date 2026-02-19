public class List {
    private int value;
    private List next;
    private int size;

    public List() {
        this.next = null;
        this.size = 0;
    }

    private List(int value) {
        this.value = value;
        this.next = null;
    }

    public void add(int value) {
        if (size == 0) {
            this.value = value;
        } else {
            List current = this;
            while (current.next != null) {
                current = current.next;
            }
            current.next = new List(value);
        }
        size++;
    }

    public void sort() {
        for (int i = 0; i < size - 1; i++) {
            List a = this;
            for (int j = 0; j < size - 1 - i; j++) {
                if (a.value > a.next.value) {
                    int temp = a.value;
                    a.value = a.next.value;
                    a.next.value = temp;
                }
                a = a.next;
            }
        }
    }

    public void print() {
        List current = this; // или head
        for (int i = 0; i < size; i++) {
            System.out.print(current.value + " ");
            current = current.next;
        }
        System.out.println();
    }

    public int get(int index) {
        List current = this;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.value;
    }

    public static List merge(List a, List b) {
        if (a == null || a.size() == 0) return b;
        if (b == null || b.size() == 0) return a;

        List result = new List();

        if (a.value <= b.value) {
            result.value = a.value;
            result.next = a.next;
            a = a.next;
        } else {
            result.value = b.value;
            result.next = b.next;
            b = b.next;
        }

        List current = result;

        while (a != null && b != null) {
            if (a.value <= b.value) {
                current.next = a;
                a = a.next;
            } else {
                current.next = b;
                b = b.next;
            }
            current = current.next;
        }

        if (a != null) {
            current.next = a;
        } else {
            current.next = b;
        }

        int count = 0;
        List temp = result;
        while (temp != null) {
            count++;
            temp = temp.next;
        }
        result.size = count;

        return result;
    }

    public int size() {
        return size;
    }

}
