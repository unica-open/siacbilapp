<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<div id="datiPagamentoDiv" class="step-pane active">
	<div class="accordion">
		<div class="accordion-group">
			<div class="accordion-heading">
				<a href="#datiPagamentoAccordion" data-parent="#datiPagamentoDiv" data-toggle="collapse" class="accordion-toggle">
					Dati pagamento<span class="icon">&nbsp;</span>
				</a>
			</div>
			<div class="accordion-body collapse" id="datiPagamentoAccordion">
				<div class="accordion-inner">
					<fieldset class="form-horizontal">
						<div class="control-group">
							<label class="control-label" for="codiceBollo">Codice bollo *</label>
							<div class="controls input-append">
								<s:select list="listaCodiceBollo" id="codiceBollo" name="documento.codiceBollo.uid" headerKey="" headerValue="" listKey="uid" listValue="%{codice + '-' + descrizione}" required="required"  />
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="tipoImpresa">Tipo impresa</label>
								<div class="controls ">
													<select class="span3" id="Codice ">
														<option>RTI</option>
														<option>ATI</option>
													</select>
												  </div>
												</div>
												
												<div class="control-group">
													<label class="control-label" for="Note">Note</label>
													<div class="controls">
														<textarea class="span10" id="Note" cols="15" rows="3"></textarea>
													</div> 
												</div>
																				
												
							
									</fieldset>
						
							</div>
						</div>
					</div>
				</div>
		</div>