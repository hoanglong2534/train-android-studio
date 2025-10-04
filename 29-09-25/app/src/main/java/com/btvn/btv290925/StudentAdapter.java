package com.btvn.btv290925;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class StudentAdapter extends BaseAdapter {
    private Context context;
    private List<Student> studentList;
    private DatabaseHelper databaseHelper;
    private OnStudentActionListener listener;

    public interface OnStudentActionListener {
        void onEditStudent(Student student);
        void onDeleteStudent(int studentId);
        void onRefreshList();
    }

    public StudentAdapter(Context context, List<Student> studentList, DatabaseHelper databaseHelper) {
        this.context = context;
        this.studentList = studentList;
        this.databaseHelper = databaseHelper;
    }

    public void setOnStudentActionListener(OnStudentActionListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return studentList.size();
    }

    @Override
    public Object getItem(int position) {
        return studentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return studentList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
            holder = new ViewHolder();
            holder.tvName = convertView.findViewById(R.id.tvName);
            holder.tvAge = convertView.findViewById(R.id.tvAge);
            holder.tvClass = convertView.findViewById(R.id.tvClass);
            holder.btnEdit = convertView.findViewById(R.id.btnEdit);
            holder.btnDelete = convertView.findViewById(R.id.btnDelete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Student student = studentList.get(position);

        // Hiển thị thông tin sinh viên
        holder.tvName.setText(student.getName());
        holder.tvAge.setText("Tuổi: " + student.getAge());
        holder.tvClass.setText("Lớp: " + student.getClassName());

        // Xử lý sự kiện nút Sửa
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onEditStudent(student);
                }
            }
        });

        // Xử lý sự kiện nút Xóa
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDeleteStudent(student.getId());
                }
            }
        });

        return convertView;
    }

    // Cập nhật danh sách
    public void updateList(List<Student> newStudentList) {
        this.studentList = newStudentList;
        notifyDataSetChanged();
    }

    // ViewHolder pattern để tối ưu hiệu suất
    static class ViewHolder {
        TextView tvName;
        TextView tvAge;
        TextView tvClass;
        Button btnEdit;
        Button btnDelete;
    }
}
