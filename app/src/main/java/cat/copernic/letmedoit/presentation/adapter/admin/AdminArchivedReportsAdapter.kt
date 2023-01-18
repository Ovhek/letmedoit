package cat.copernic.letmedoit.presentation.adapter.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.letmedoit.data.model.Report
import cat.copernic.letmedoit.databinding.ItemAdminReportListBinding
import cat.copernic.letmedoit.presentation.adapter.admin.viewholder.AdminArchivedReportsViewHolder
class AdminArchivedReportsAdapter(
    val reportList: List<Report>,
) : RecyclerView.Adapter<AdminArchivedReportsViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdminArchivedReportsViewHolder {
        val binding =
            ItemAdminReportListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminArchivedReportsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdminArchivedReportsViewHolder, position: Int) {
        val item = reportList[position]
        holder.render(item)

    }

    override fun getItemCount(): Int = reportList.size


}