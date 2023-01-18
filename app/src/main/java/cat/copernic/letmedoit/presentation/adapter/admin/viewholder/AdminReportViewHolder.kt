package cat.copernic.letmedoit.presentation.adapter.admin.viewholder

import androidx.recyclerview.widget.RecyclerView
import cat.copernic.letmedoit.data.model.Report
import cat.copernic.letmedoit.databinding.ItemAdminReportListBinding
/**
 * ViewHolder de reportes
 */
class AdminReportViewHolder(val binding: ItemAdminReportListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val userA = binding.txtUser
    val userReported = binding.txtUserReported

    fun render(
        reportModel: Report,
        onClickCheckBox:(Report) -> Unit,
    ) {
        userA.text = reportModel.user_1
        userReported.text = reportModel.user_2
        binding.checkBoxReport.setOnClickListener{onClickCheckBox(reportModel)}
    }
}