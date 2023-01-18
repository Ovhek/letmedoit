package cat.copernic.letmedoit.presentation.adapter.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.letmedoit.data.model.Report
import cat.copernic.letmedoit.databinding.ItemAdminReportListBinding
import cat.copernic.letmedoit.presentation.adapter.admin.viewholder.AdminReportViewHolder
/**
 * Adapter de reportes
 */
class AdminReportAdapter(
    val reportList: List<Report>,
    private val onClickCheckBox:(Report) -> Unit,
) : RecyclerView.Adapter<AdminReportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminReportViewHolder {
        val binding =
            ItemAdminReportListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminReportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdminReportViewHolder, position: Int) {
        val item = reportList[position]
        holder.render(item,onClickCheckBox)
    }

    override fun getItemCount(): Int = reportList.size

}