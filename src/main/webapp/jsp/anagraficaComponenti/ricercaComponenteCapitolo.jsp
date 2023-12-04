<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />
</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />
 
 	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="contentPage">
				<s:include value="/jsp/include/messaggi.jsp" />
					<s:form action="effettuaRicercaComponenteCapitolo" novalidate="novalidate" id="formRicercaComponenteCapitolo">
						<h3>Tabella Componenti</h3>
						<div class="step-content">
						<p><s:submit cssClass="btn btn-primary pull-right" value="cerca" /></p>
							<div class="fieldset-body">
									<fieldset id="formRicercaProvvedimento" class="form-horizontal">
										<br>
										<div class="control-group">
											<label class="control-label" for="compCapMacrotipo">Macrotipo</label>
											<div class="controls">
												<s:select list="listaMacroTipo"  required="false" headerKey="" headerValue="" listValue="descrizione" name="componenteCapitolo.macrotipoComponenteImportiCapitolo" id="componenteCapitolo.macrotipoComponenteImportiCapitolo.codice" cssClass="lbTextSmall span3"/>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label" for="compCapMacrotipo">Sottotipo</label>
											<div class="controls">
												<s:select list="listaSottoTipo" required="false" headerKey="" headerValue=""   listValue="descrizione" name="componenteCapitolo.sottotipoComponenteImportiCapitolo" id="componenteCapitolo.sottotipoComponenteImportiCapitolo.codice" cssClass="lbTextSmall span3"/>
											</div>
										</div>
		
										<div class="control-group">
											<label class="control-label" for="descrizioneComponente">Descrizione</label>
											<div class="controls">
												<s:textarea rows="1" cols="15" id="descrizioneComponente" name="componenteCapitolo.descrizione" class="span10" maxlength="500" ></s:textarea>
											</div>
										</div>
										<%-- SIAC-7873 --%>
										<s:hidden name="saltaControlloSuDateValidita" value="true"></s:hidden>
									</fieldset>
								</div>
							</div>
							<p class="margin-large">
								<s:include value="/jsp/include/indietro.jsp" />
								<s:reset cssClass="btn btn-link" value="annulla" />
								<s:submit cssClass="btn btn-primary pull-right" value="cerca" />
							</p>
					</s:form>
				</div>
			</div>
		</div>
	</div>
 
	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ztree.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ricerca.js"></script>

</body>
</html>