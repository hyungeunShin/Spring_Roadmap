package hello.typeconverter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import hello.typeconverter.type.IpPort;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Controller
public class ConverterController {
	@GetMapping("/converter-view")
	public String converterView(Model model) {
		model.addAttribute("number", 10000);
		model.addAttribute("ipPort", new IpPort("127.0.0.1", 80));
		return "converter-view";
	}
	
	@GetMapping("/converter/edit")
	public String convertForm(Model model) {
		IpPort ipPort = new IpPort("127.0.0.1", 80);
		Form form = new Form(ipPort);
		model.addAttribute("form", form);
		return "converter-form";
	}
	
	@PostMapping("/converter/edit")
	public String convertEdit(@ModelAttribute Form form, Model model) {
		IpPort ipPort = form.getIpPort();
		model.addAttribute("ipPort", ipPort);
		return "converter-view";
	}
	
	@Getter
	@Setter
	@ToString
	@NoArgsConstructor
	static class Form {
		private IpPort ipPort;

		public Form(IpPort ipPort) {
			this.ipPort = ipPort;
		}
	}
}
