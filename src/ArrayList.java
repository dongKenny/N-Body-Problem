public class ArrayList<T> implements List<T> {
    int size;
    int capacity;
    T[] array;

    public ArrayList(int capacity) {
        array = (T[]) new Object[capacity];
        this.capacity = capacity;
        size = 0;
    }

    @Override
    public boolean add(T element) {
        if (array[capacity-1] == null) {
            size++;
            array[size - 1] = element;
        }
        else if (array[capacity-1] != null){
            T[] array2 = (T[]) new Object[capacity*2];
            for (int i = 0; i < array.length; i++) {
                array2[i] = array[i];
            }
            array2[capacity] = element;
            capacity *= 2;
            size++;
            array = array2;
        }

        return true;
    }

    @Override
    public void add(int index, T element) throws Exception{
        if (index >= capacity || index < 0) {
            throw new Exception("Error: Specified Index Out Of Range (for add)");
        }
        else if (index < capacity && array[capacity-1] != null) {
            T[] array2 = (T[]) new Object[capacity*2];
            for (int i = 0; i < index; i++) {
                array2[i] = array[i];
            }
            array2[index] = element;
            for (int i = array2.length / 2 ; i > index; i--) {
                array2[i] = array[i - 1];
            }
            capacity *= 2;
            size++;
            array = array2;
        }
        else if (index < capacity && array[capacity-1] == null) {
            for (int i = array.length - 1; i > index; i--) {
                array[i] = array[i-1];
            }
            array[index] = element;
        }
    }

    @Override
    public T get(int index) {
        return array[index];
    }

    @Override
    public T remove(int index) throws Exception {
        T removed = array[index];

        if (index >= size || index < 0) {
            throw new Exception("Error: Specified Index Out Of Range");
        }
        else {
            for (int i = index; i < array.length - 1; i++) {
                array[i] = array[i+1];
            }
            array[capacity - 1] = null;
            size--;
            return removed;
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public String toString() {
        String output = "";
        output += '[';
        for (int i = 0; i < array.length - 1; i++) {
            output += array[i] + ", ";
        }
        output += array[array.length-1];
        output += ']';
        return output;
    }
}
