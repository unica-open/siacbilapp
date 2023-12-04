<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />
</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage" id="accordion2">
				<s:include value="/jsp/include/messaggi.jsp" />
 				<h3>Aggiorna Variazione</h3>
				<s:form cssClass="form-horizontal form-disabilitato" novalidate="novalidate" id="aggiornamentoVariazioneCodifiche" method="post">
					<input type="hidden" id="tipoAzione" value="Aggiornamento"/>
					<div class="accordion-group">
						<div class="accordion-heading">
							<a class="accordion-toggle" href="#collapseVariazioni" data-parent="#accordion2" data-toggle="collapse">
								Variazione<span class="icon"></span>
							</a>
						</div>
						<div id="collapseVariazioni" class="accordion-body in collapse" style="height: auto;">
							<div class="accordion-inner">
								<dl class="dl-horizontal">
									<dt>Num. variazione</dt>
									<dd>&nbsp;<s:property value="numeroVariazione" /></dd>
									<dt>Stato</dt>
									<dd>&nbsp;<s:property value="elementoStatoOperativoVariazione.descrizione" /></dd>
									<dt>Applicazione</dt>
									<dd>&nbsp;<s:property value="applicazione" /></dd>
									<dt>Tipo Variazione</dt>
									<dd>&nbsp;<s:property value="tipoVariazione.codice" />&nbsp;-&nbsp;<s:property value="tipoVariazione.descrizione" /></dd>
								</dl>
								<h5>Elenco modifiche in variazione</h5>    
								<table class="table table-condensed table-hover table-bordered" id="codificheNellaVariazione" summary="....">
									<thead>
										<tr>
											<th scope="col">Capitolo</th>
											<th scope="col">Descrizione capitolo</th>
											<th scope="col">Descrizione articolo</th>
											<th scope="col">Struttura amministrativa</th>
											<th scope="col">&nbsp;</th>
											<th scope="col">&nbsp;</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
								<h5>Aggiorna le note e/o la descrizione</h5>
								<div class="control-group">
									<label class="control-label" for="descrizioneVariazione">Descrizione *</label>
									<div class="controls">
										<s:textfield id="descrizioneVariazione" placeholder="descrizione" cssClass="span10" name="descrizione" maxlength="500" required="true" />
									</div>
								</div>
								<div class="control-group">
									<label for="noteVariazione" class="control-label">Note</label>
									<div class="controls">
										<s:textarea rows="2" cols="55" cssClass="span10" id="noteVariazione" name="note" maxlength="500"></s:textarea>
									</div>
								</div>
								
								<div class="accordion margin-large" id="accordion5">
									<div class="accordion-group">
										<div class="accordion-heading">
											<a class="accordion-toggle disabled collapsed" data-toggle="collapse" data-parent="#accordion4" href="#collapseProvvedimento">
												<s:property value="stringaProvvedimento"/>
											</a>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					
					<p class="margin-large">
						<s:include value="/jsp/include/indietro.jsp"/>&nbsp;
						<s:if test="salvaAbilitato">
							<span class="nascosto"> | </span>
							<a class="btn" id="pulsanteSalvaAggiornamentoVariazione" href="#">salva</a>&nbsp;
						</s:if>
						<s:if test="annullaAbilitato">
							<span class="nascosto"> | </span>
							<a href='#msgAnnulla' title='annulla variazione' role='button' class="btn" data-toggle='modal'>annulla variazione</a>
						</s:if>
						<s:if test="concludiAbilitato">
							<span class="nascosto"> | </span>
							<a class="btn btn-primary pull-right" id="pulsanteConcludiAggiornamentoVariazione" href="#">concludi attivit&agrave;</a>&nbsp;
						</s:if>
					</p>
				</s:form>
			</div>
		</div>
	</div>
	
	<%-- Modale annulla variazione --%>
	<div id="msgAnnulla" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
		<div class="modal-body">
			<div class="alert alert-error">
				<p>
					<strong>Attenzione!!!</strong>
				</p>
				<p>Stai per annullare la variazione: sei sicuro di voler proseguire?</p>
			</div>
		</div>
		<div class="modal-footer">
			<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
			<button class="btn btn-primary" id="EDIT_annulla">s&iacute;, prosegui</button>
		</div>
	</div>
	<%-- /Modale annulla --%>
	
	<s:include value="/jsp/include/footer.jsp"/>
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ztree.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ricerca_collapse.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/variazioni/variazioni.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/variazioni/ztree.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/variazioni/classificatori.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/variazioni/aggiorna.codifiche.js"></script>
	
</body>
</html>